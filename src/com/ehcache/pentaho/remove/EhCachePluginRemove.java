package com.ehcache.pentaho.remove;


/*
EhCache Pentaho Plugin
Author: Stephan Grotz (stephan.grotz@gmail.com)
URL: https://github.com/sgrotz/PentahoEhCachePlugin

Copyright (c) 2012 Stephan Grotz

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and 
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of 
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO 
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

*/


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


public class EhCachePluginRemove extends BaseStep implements StepInterface {

	public EhCachePluginRemove(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans) {
		
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
		// TODO Auto-generated constructor stub
	}

	private EhCachePluginRemoveData data;
	private EhCachePluginRemoveMeta meta;
	private CacheManager manager;
	private Cache cache;

	@Override
	public void dispose(StepMetaInterface iMeta, StepDataInterface iData) {
		// TODO Auto-generated method stub

		meta = (EhCachePluginRemoveMeta) iMeta;
		data = (EhCachePluginRemoveData) iData;

		// manager.shutdown();

		super.dispose(iMeta, iData);

	}

	@Override
	public boolean init(StepMetaInterface iMeta, StepDataInterface iData) {
		// TODO Auto-generated method stub

		meta = (EhCachePluginRemoveMeta) iMeta;
		data = (EhCachePluginRemoveData) iData;

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
		} else {
			logError("No cacheName or XML URL was specified ...");
		}

		return super.init(iMeta, iData);
	}


	@Override
	public boolean processRow(StepMetaInterface iMeta, StepDataInterface iData)
			throws KettleException {
		// TODO Auto-generated method stub

		meta = (EhCachePluginRemoveMeta) iMeta;
		data = (EhCachePluginRemoveData) iData;

		// Make sure to check that the cache exists...
		if (!manager.cacheExists(meta.getCacheName())) {
			logError("*** Cache " + meta.getCacheName() + " does not exist in the ehcache.xml configuration file... ");
			setOutputDone();
			return false;
		}
		
		// If all objects have been processed, exit here ...
		Object[] obj = getRow();
		if (obj==null) {
			logDebug("*** There is no more input ... Nothing more to delete from the cache! :(");
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
		
		// Get the key from the input rows
		String key = data.outputRowMeta.getString(obj, data.outputRowMeta.indexOfValue("KEY"));

		if (key != null) {
			cache = manager.getCache(meta.getCacheName());
	
			// Removing the element ...
			logDebug("*** Removing Element " + key + " from the cache ...");
			cache.remove(key);
		
			incrementLinesWritten();
				
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
