package com.ehcache.pentaho.remove;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.BaseStepData.StepExecutionStatus;
import org.pentaho.di.trans.step.StepDataInterface;

public class EhCachePluginRemoveData extends BaseStepData implements StepDataInterface {
	
	public RowMetaInterface outputRowMeta;
	public RowMetaInterface inputRowMeta;
	
    public EhCachePluginRemoveData()
	{
		super();
	}
	

}
