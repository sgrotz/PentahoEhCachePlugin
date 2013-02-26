package com.ehcache.pentaho.dummyPojo;


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
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
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
import org.pentaho.di.core.row.ValueMetaAndData;


public class EhCachePluginDummyPojo extends BaseStep implements StepInterface {

	public EhCachePluginDummyPojo(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans) {
		
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
		// TODO Auto-generated constructor stub
	}

	private EhCachePluginDummyPojoData data;
	private EhCachePluginDummyPojoMeta meta;
	private CacheManager manager;
	private Cache cache;

	@Override
	public void dispose(StepMetaInterface iMeta, StepDataInterface iData) {
		// TODO Auto-generated method stub

		meta = (EhCachePluginDummyPojoMeta) iMeta;
		data = (EhCachePluginDummyPojoData) iData;

		// manager.shutdown();

		super.dispose(iMeta, iData);

	}

	@Override
	public boolean init(StepMetaInterface iMeta, StepDataInterface iData) {
		// TODO Auto-generated method stub

		meta = (EhCachePluginDummyPojoMeta) iMeta;
		data = (EhCachePluginDummyPojoData) iData;

		String cacheName = meta.getCacheName();
		String xmlURL = meta.getXmlURL();


		if ((xmlURL == null) || (cacheName == null)) {
			meta.setDefault();
		}

		logDebug("*** Using Cache: " + cacheName + " from configuration file: " + xmlURL);

		if ((cacheName != null) && (xmlURL != null)) {
			manager = CacheManager.newInstance(xmlURL);
			cache = manager.getCache(cacheName);
		} else {
			logError("*** No cacheName or XML URL was specified ...");
		}

		return super.init(iMeta, iData);
	}


	@Override
	public boolean processRow(StepMetaInterface iMeta, StepDataInterface iData)
			throws KettleException {
		// TODO Auto-generated method stub

		meta = (EhCachePluginDummyPojoMeta) iMeta;
		data = (EhCachePluginDummyPojoData) iData;
		
		// Make sure to check the cache exists before processing the entries ...
		if (!manager.cacheExists(meta.getCacheName())) {
			logError("*** Cache " + meta.getCacheName() + " does not exist in the ehcache.xml configuration file... ");
			
			// If the cache does not exist, cancel all operations and exit!
			setErrors(1);
			stopAll();
		}
		
		// If all objects have been processed, exit here ... 
		Object[] obj = getRow();
		if (obj==null) {
			logDebug("*** There is no more input ... Nothing to add to the cache! :(");
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

		// ###
		// ### STARTING FROM HERE, ADD YOUR OWN POJO IMPLEMENTATION
		// ### Make sure to place your POJO definition in the classpath, reference your object and map it into 
		// ### the ELEMENT. With this, you can also define searchable attributes. 
		// ### The following sample uses the SampleObject class.
		// ###
		
		
		/*
		 * The following code is just a sample - how an object can be written into the cache
		 * 
		 */ 
		 
		SampleObject myObject = new SampleObject();
		
		// Map the ID, firstname and lastname from the input row into your object ...
		myObject.setID(data.outputRowMeta.getNumber(obj, data.outputRowMeta.indexOfValue("ID")));
		myObject.setFirstName(data.outputRowMeta.getString(obj, data.outputRowMeta.indexOfValue("FIRSTNAME")));
		myObject.setLastName(data.outputRowMeta.getString(obj, data.outputRowMeta.indexOfValue("LASTNAME")));


		if ((myObject.getID() != null)) {
			cache = manager.getCache(meta.getCacheName());
			
			
			// Add the elements to the cache...
			logDebug("*** Writing new SampleObject " + myObject.getID().toString() + " ...");     
			cache.put(new Element(myObject.getID(), myObject));

			if (checkFeedback(linesRead)) logBasic("Linenr "+linesRead);

			incrementLinesWritten();
			return true;
		} else {
			// Throw an error when the ID or Value was null
			logError("*** KEY or VALUE field was null ...");     
			return false;
		} 
		
		
		// ###
		// ### POJO IMPLEMENTATION END 
		// ###
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
