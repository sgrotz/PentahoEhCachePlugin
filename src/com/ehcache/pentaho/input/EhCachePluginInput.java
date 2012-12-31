package com.ehcache.pentaho.input;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.ResultFile;
import org.pentaho.di.core.RowSet;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaAndData;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.BaseStepData.StepExecutionStatus;
import org.pentaho.di.trans.step.RowListener;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepListener;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;


public class EhCachePluginInput extends BaseStep implements StepInterface {

	public EhCachePluginInput(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans) {
		
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
		// TODO Auto-generated constructor stub
	}

	private EhCachePluginInputData data;
	private EhCachePluginInputMeta meta;
	private CacheManager manager;
	private Cache cache;

	@Override
	public void dispose(StepMetaInterface iMeta, StepDataInterface iData) {
		// TODO Auto-generated method stub

		meta = (EhCachePluginInputMeta) iMeta;
		data = (EhCachePluginInputData) iData;

		// manager.shutdown();

		super.dispose(iMeta, iData);

	}

	@Override
	public boolean init(StepMetaInterface iMeta, StepDataInterface iData) {
		// TODO Auto-generated method stub

		meta = (EhCachePluginInputMeta) iMeta;
		data = (EhCachePluginInputData) iData;

		String cacheName = meta.getCacheName();
		String xmlURL = meta.getXmlURL();


		if ((xmlURL == null) || (cacheName == null)) {
			meta.setDefault();
		}
			logDebug("*** Using Cache: " + cacheName + " from configuration file: " + xmlURL);


		if ((cacheName != null) && (xmlURL != null)) {
			//manager = CacheManager.newInstance("plugins/steps/ehCachePlugin/ehcache.xml");
			manager = CacheManager.newInstance(xmlURL);
			cache = manager.getCache(cacheName);
		}


		return super.init(iMeta, iData);
	}


	@Override
	public boolean processRow(StepMetaInterface iMeta, StepDataInterface iData)
			throws KettleException {
		// TODO Auto-generated method stub

		meta = (EhCachePluginInputMeta) iMeta;
		data = (EhCachePluginInputData) iData;

		if (!manager.cacheExists(meta.getCacheName())) {
			logError("*** Cache " + meta.getCacheName() + " does not exist in the ehcache.xml configuration file... ");
			setOutputDone();
			return false;
		}
		
		Object[] obj = getRow();
		if (obj==null) {
			logDebug("*** There is no more input ... Nothing to get from the cache! :(");
			setOutputDone();
			return false;
		}
		
		if (first)
		{
			logDebug("*** This is the first record ...");
			first = false;
			data.outputRowMeta = (RowMetaInterface)getInputRowMeta().clone();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this); 
		}
		
		String key = data.outputRowMeta.getString(obj, data.outputRowMeta.indexOfValue("KEY"));

		if (key != null) {
			cache = manager.getCache(meta.getCacheName());
	
			logDebug("*** Getting Element " + key + " from the cache ...");
			Element element = cache.get(key);
		
			if (element != null) {
				// If not null - add to the output ...
				Object[] outputRow = RowDataUtil.addValueData(obj, data.outputRowMeta.size()-1, element.getObjectValue());
				putRow(data.outputRowMeta, outputRow);
				
				incrementLinesWritten();	
			} else {
				// Log an error that the element is not existing ... 
				logError("*** Element " + key + " does not exist anymore ...");
			}
	
		    // Some basic logging
		    if (checkFeedback(getLinesRead())) {
		        if (log.isBasic()) logBasic("Linenr " + getLinesRead()); 
		    }
		 
		    return true;
		} else {
			logError("*** KEY field was null ..."); 
			return false;
		}
		
	}


	@SuppressWarnings("deprecation")
	public void run()
	{
		logBasic("*** Starting to run...");
		try
		{
			while (processRow(meta, data) && !isStopped());
		}
		catch(Exception e)
		{
			logError("Unexpected error : "+e.toString());
			logError(Const.getStackTracker(e));
			setErrors(1);
			stopAll();
		}
		finally
		{
			dispose(meta, data);
			logBasic("Finished, processing "+linesRead+" rows");
			markStop();
		}
	}


}
