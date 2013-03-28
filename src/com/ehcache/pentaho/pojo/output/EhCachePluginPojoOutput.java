package com.ehcache.pentaho.pojo.output;


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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;


/**
 * @author sgrotz
 *
 */
public class EhCachePluginPojoOutput extends BaseStep implements StepInterface {

	public EhCachePluginPojoOutput(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans) {

		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
		// TODO Auto-generated constructor stub
	}

	private EhCachePluginPojoOutputData data;
	private EhCachePluginPojoOutputMeta meta;
	private CacheManager manager;
	private Cache cache;

	
	
	/* (non-Javadoc)
	 * @see org.pentaho.di.trans.step.BaseStep#dispose(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)
	 */
	@Override
	public void dispose(StepMetaInterface iMeta, StepDataInterface iData) {
		// TODO Auto-generated method stub

		meta = (EhCachePluginPojoOutputMeta) iMeta;
		data = (EhCachePluginPojoOutputData) iData;

		if (meta.isUseBulkApi()) {
			cache.setNodeBulkLoadEnabled(false);
		}
		manager.shutdown();
		
		super.dispose(iMeta, iData);

	}
	
	

	/* (non-Javadoc)
	 * @see org.pentaho.di.trans.step.BaseStep#init(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)
	 */
	@Override
	public boolean init(StepMetaInterface iMeta, StepDataInterface iData) {
		// TODO Auto-generated method stub

		meta = (EhCachePluginPojoOutputMeta) iMeta;
		data = (EhCachePluginPojoOutputData) iData;

		String cacheName = meta.getCacheName();
		String xmlURL = meta.getXmlURL();


		if ((xmlURL == null) || (cacheName == null)) {
			meta.setDefault();
		}

		logDebug("*** Using Cache: " + cacheName + " from configuration file: " + xmlURL);

		if ((cacheName != null) && (xmlURL != null)) {
			manager = CacheManager.newInstance(xmlURL);
			cache = manager.getCache(cacheName);
			if (meta.isUseBulkApi()) {
				cache.setNodeBulkLoadEnabled(true);
			}
		} else {
			logError("*** No cacheName or XML URL was specified ...");
		}

		return super.init(iMeta, iData);
	}


	/* (non-Javadoc)
	 * @see org.pentaho.di.trans.step.BaseStep#processRow(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)
	 */
	@Override
	public boolean processRow(StepMetaInterface iMeta, StepDataInterface iData)
			throws KettleException {
		// TODO Auto-generated method stub

		if (manager == null) {
			init(iMeta, iData);
		}

		meta = (EhCachePluginPojoOutputMeta) iMeta;
		data = (EhCachePluginPojoOutputData) iData;

		String className = meta.getClassName();
		String idFieldName = meta.getIdFieldName();

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

		Object transformedObject = null;

		try {
			transformedObject = transformRowToObject(Class.forName(className), obj, meta, data);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long ID = 0;
		try {
			ID = getObjectID(idFieldName, obj);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if ((ID != 0) && (transformedObject != null)) {
			cache = manager.getCache(meta.getCacheName());

			// Add the elements to the cache...
			logDebug("*** Writing new Java Object as element: " + ID );     
			cache.put(new Element(ID, transformedObject));

			if (checkFeedback(linesRead)) logBasic("Linenr "+linesRead);

			incrementLinesWritten();
			return true;
		} else {
			// Throw an error when the ID or Value was null
			logError("*** KEY or Object was null ...");     
			return false;
		}
	}


	
	/**
	 * Method to transform an incoming row into an object. Special thanks to Woijtek, for his initial code, which I could use as a baseline :)
	 * Please make sure that the incoming fields are named exactly as the setter method in the class file. Otherwise, the mapper will ignore the data. 
	 * 
	 * @param objectClass
	 * @param rowObject
	 * @param iMeta
	 * @param iData
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws KettleValueException
	 */
	private Object transformRowToObject(Class objectClass, Object[] rowObject, StepMetaInterface iMeta, StepDataInterface iData) throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, KettleValueException {
		
		// Initializing a map for all public setter methods
		Map<String, Method> setterMethodMap = new HashMap<String, Method>();
		Map<String, String> fieldMap = new HashMap<String, String>();

		// Determine all public set methods
		Method[] methods = objectClass.getMethods();
		for (Method method : methods) {
			// Only add this method if it is part of the class itself and if it's a setter
			if(method.toGenericString().contains(objectClass.getCanonicalName()) && method.getName().startsWith("set")){
				setterMethodMap.put(method.getName(), method);
			}
		}

		// Map all incoming fields into a map - remove the "_"
		String[] fieldNames = data.outputRowMeta.getFieldNames();
		for (String field : fieldNames) {
			logDebug("Added new fieldname to the fieldList: " + field.replaceAll("_", "").toUpperCase());
			fieldMap.put(field.replaceAll("_", "").toUpperCase(), field);
		}

		// Create new object
		Object object = objectClass.newInstance();

		int setterCount = setterMethodMap.size();
		logDebug("*** Object " + objectClass.getName() + " has " + setterCount + " set methods...");     

		// Loop over all entries in the setterMap
		for (Map.Entry<String, Method> entry : setterMethodMap.entrySet()) {

			// the SetterName is the method name in CAPITAL, without the first three letters
			String setterName = entry.getKey().substring(3).toUpperCase();

			Method setterMethod = entry.getValue();
			String fieldName = null;

			// First check if the field map contains a key for the setterName
			if (fieldMap.containsKey(setterName)){
				fieldName = fieldMap.get(setterName);
				logDebug("*** Method " + entry.getKey() + " will be mapped with data from Field "+ fieldName);

				// Find out which type of object is expected in the setter method
				Class<?> parameterTypeClass = setterMethod.getParameterTypes()[0];

				if (parameterTypeClass.equals(String.class) ){
					String stringValue = data.outputRowMeta.getString(rowObject, data.outputRowMeta.indexOfValue(fieldName));
					if (stringValue != null) {
						logDebug("*** Setting " + setterName + " value as String: " + stringValue);
						setterMethod.invoke(object, stringValue);
					}

				} else if (parameterTypeClass.equals(Character.class) || parameterTypeClass.equals(char.class)){
					String charValue = data.outputRowMeta.getString(rowObject, data.outputRowMeta.indexOfValue(fieldName));
					if (charValue != null) {
						logDebug("*** Setting " + setterName + " value as String: " + charValue);
						setterMethod.invoke(object, charValue.charAt(0));
					}

				} else if (parameterTypeClass.equals(Integer.class) || parameterTypeClass.equals(int.class)){
					String intValue = String.valueOf(data.outputRowMeta.getInteger(rowObject, data.outputRowMeta.indexOfValue(fieldName)));

					if (intValue == null || intValue.equals("null")){
					} else {
						logDebug("*** Setting " + setterName + " value as Integer: " +  Integer.valueOf(intValue));
						setterMethod.invoke(object, Integer.valueOf(intValue));
					}

				} else if (parameterTypeClass.equals(long.class) || parameterTypeClass.equals(Long.class)){

					String longValue = String.valueOf(data.outputRowMeta.getInteger(rowObject, data.outputRowMeta.indexOfValue(fieldName)));
					if (longValue == null || longValue.equals("null")){
					} else {
						logDebug("*** Setting " + setterName + " value as Long: " + Long.valueOf(longValue));
						setterMethod.invoke(object, Long.valueOf(longValue));
					}

				} else if (parameterTypeClass.equals(Boolean.class) || parameterTypeClass.equals(boolean.class)){
					boolean boolValue = data.outputRowMeta.getBoolean(rowObject, data.outputRowMeta.indexOfValue(fieldName));
					logDebug("*** Setting " + setterName + " value as Boolean: " + boolValue);
					setterMethod.invoke(object, boolValue);

				} else if (parameterTypeClass.equals(Date.class)){
					Date dateValue = data.outputRowMeta.getDate(rowObject, data.outputRowMeta.indexOfValue(fieldName));
					if (dateValue != null) {
						logDebug("*** Setting " + setterName + " value as Date: " + dateValue);
						setterMethod.invoke(object, dateValue);
					}

				} else if (parameterTypeClass.equals(Double.class)){
					Double doubleValue = data.outputRowMeta.getNumber(rowObject, data.outputRowMeta.indexOfValue(fieldName));
					if (doubleValue != null) {
						logDebug("*** Setting " + setterName + " value as Number: " + doubleValue);
						setterMethod.invoke(object, doubleValue );
					}

				} else if (parameterTypeClass.equals(BigDecimal.class)){
					BigDecimal bigValue = data.outputRowMeta.getBigNumber(rowObject, data.outputRowMeta.indexOfValue(fieldName));
					if (bigValue != null) {
						logDebug("*** Setting " + setterName + " value as Big Decimal: " + bigValue);
						setterMethod.invoke(object, bigValue);
					}

				} else {
					// TODO Do this correctly.
					throw new UnsupportedOperationException("Unsupported type found: " + parameterTypeClass.getCanonicalName() + " for field: "+ fieldName);
				}
			} 

		}

		logDebug("*** Finished mapping object " + objectClass.getName());    

		return object;
	}

	public long getObjectID(String idFieldName, Object[] rowObject) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, KettleValueException {

		long returnValue = data.outputRowMeta.getInteger(rowObject, data.outputRowMeta.indexOfValue(idFieldName));
		return returnValue;

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
