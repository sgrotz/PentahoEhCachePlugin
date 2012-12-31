package com.ehcache.pentaho.listKeys;

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

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;


@Step(name="EhCache List Keys",
	image="plugins/steps/ehCachePlugin/ehcache.png",
	description="List all cache keys ...", 
	categoryDescription="Terracotta", 
	id = "ehcachelist")

public class EhCachePluginListMeta extends BaseStepMeta implements StepMetaInterface {

	private static Class<?> PKG = EhCachePluginListMeta.class;
	
	private  String cacheName;
	private  String xmlURL;
	
	public String getCacheName() {
		return cacheName;
	}


	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}


	public String getXmlURL() {
		return xmlURL;
	}


	public void setXmlURL(String xmlURL) {
		this.xmlURL = xmlURL;
	}

	
	public Object clone()
	{
		Object retval = super.clone();
		return retval;
	}
	
	
	@Override
	public void check(List<CheckResultInterface> remarks, TransMeta arg1,
			StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info) {
		// TODO Auto-generated method stub
		
		CheckResult cr;
		if (prev==null || prev.size()==0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, "Not receiving any fields from previous steps!", stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is connected to previous one, receiving "+prev.size()+" fields", stepMeta);
			remarks.add(cr);
		}
		
		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta);
			remarks.add(cr);
		}
		
		
	}
	
	public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
	{
	    // append the outputFields to the output
	    ValueMetaInterface id = new ValueMeta("KEYS", ValueMetaInterface.TYPE_STRING);
	 
	    //id.setOrigin(origin);
	    r.addValueMeta(id);
	    
	}
	
	public String getXML() throws KettleException
	{
        StringBuffer retval = new StringBuffer(150);
        
        retval.append("    ").append(XMLHandler.addTagValue("cacheName", cacheName));
        retval.append("    ").append(XMLHandler.addTagValue("xmlURL", xmlURL));

		return retval.toString();
	}

	@Override
	public void loadXML(Node node, List<DatabaseMeta> arg1,
			Map<String, Counter> arg2) throws KettleXMLException {
		// TODO Auto-generated method stub
		
		try
		{
			cacheName = XMLHandler.getTagValue(node, "cacheName");
			xmlURL = XMLHandler.getTagValue(node, "xmlURL");
		
		}
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to read step info from XML node", e);
		}
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void readRep(Repository rep, ObjectId id_step,
			List<DatabaseMeta> arg2, Map<String, Counter> arg3)
			throws KettleException {
		// TODO Auto-generated method stub
		
		cacheName = rep.getStepAttributeString(id_step, "cacheName");
		xmlURL = rep.getStepAttributeString(id_step, "xmlURL");
		
	}

	@Override
	public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step)
			throws KettleException {

		rep.saveStepAttribute(id_transformation, id_step, "cacheName", cacheName); 
        rep.saveStepAttribute(id_transformation, id_step, "xmlURL", xmlURL); 

		
	}

	@Override
	public void setDefault() {
		// TODO Auto-generated method stub
		
		cacheName = "TestCache";
		xmlURL = "plugins/steps/ehCachePlugin/ehcache.xml";
		
	}
	
	public EhCachePluginListDialog getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) throws KettleValueException {
		return new EhCachePluginListDialog(shell, meta, transMeta, name);
		
	}
	

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp)
	{
		return new EhCachePluginList(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	public StepDataInterface getStepData()
	{
		return new EhCachePluginListData();
	}
	
	
	public EhCachePluginListMeta()
	{
		super();
	}

}
