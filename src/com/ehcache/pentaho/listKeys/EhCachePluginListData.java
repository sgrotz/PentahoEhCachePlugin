package com.ehcache.pentaho.listKeys;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class EhCachePluginListData extends BaseStepData implements StepDataInterface {
	
	public RowMetaInterface outputRowMeta;

    public EhCachePluginListData()
	{
		super();
	}
	

}
