package com.ehcache.pentaho.listKeys;

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


public class EhCachePluginList extends BaseStep implements StepInterface {

	public EhCachePluginList(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans) {
		
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
		// TODO Auto-generated constructor stub
	}

	private EhCachePluginListData data;
	private EhCachePluginListMeta meta;
	private CacheManager manager;
	private Cache cache;

	@Override
	public void dispose(StepMetaInterface iMeta, StepDataInterface iData) {
		// TODO Auto-generated method stub

		meta = (EhCachePluginListMeta) iMeta;
		data = (EhCachePluginListData) iData;

		// manager.shutdown();

		super.dispose(iMeta, iData);

	}

	@Override
	public boolean init(StepMetaInterface iMeta, StepDataInterface iData) {
		// TODO Auto-generated method stub

		meta = (EhCachePluginListMeta) iMeta;
		data = (EhCachePluginListData) iData;

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

		meta = (EhCachePluginListMeta) iMeta;
		data = (EhCachePluginListData) iData;

		if (!manager.cacheExists(meta.getCacheName())) {
			logError("*** Cache " + meta.getCacheName() + " does not exist in the ehcache.xml configuration file... ");
			setOutputDone();
			return false;
		}
		
		cache = manager.getCache(meta.getCacheName());
		List keys = cache.getKeys();
		
		logDebug("*** Found " + keys.size() + " Elements: " + keys.toString());
		Object[] obj = new Object[keys.size()];
		
		Iterator it = keys.iterator();
		int i = 0;
		
		//data.outputRowMeta = (RowMetaInterface)getInputRowMeta().clone();
		//meta.getFields(data.outputRowMeta, getStepname(), null, null, this); 
		
		Object[] outputRow = RowDataUtil.resizeArray(obj, keys.size());
		
		while (it.hasNext()) {
		
			Element e = cache.get(it.next());
			
			String key = e.getObjectKey().toString();
			//Object[] obj = getRow();
			logDebug("*** Adding Key " + e.getObjectKey() + " to the output list ...");
			outputRow = RowDataUtil.addValueData(outputRow, 0, key);
		
			putRow(data.outputRowMeta, outputRow);
			i++;
			
			//logDebug("Output row size is: " + outputRow.length + outputRow.toString());
			
		    // Some basic logging
		    if (checkFeedback(getLinesRead())) {
		        if (log.isBasic()) logBasic("Linenr " + getLinesRead()); 
		    }
		}
	 
		
		setOutputDone();
	    return false;
		
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
