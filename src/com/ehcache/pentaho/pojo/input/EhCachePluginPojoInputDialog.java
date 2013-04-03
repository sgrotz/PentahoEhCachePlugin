package com.ehcache.pentaho.pojo.input;


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



import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import com.ehcache.pentaho.*;

public class EhCachePluginPojoInputDialog extends BaseStepDialog implements StepDialogInterface {

	private EhCachePluginPojoInputMeta input;
	private String cacheName;
	private String xmlURL;
	private  String className;
	private  String idFieldName;
	private boolean useBulkLoad;
	private Text txEhcachePath;
	private Text txCacheName;
	private Text txObjectClass;
	private Text txUniqueID;
	private Button btBulkLoad; 
	private Text txStepName;

	public EhCachePluginPojoInputDialog(Shell parent, Object object,
			TransMeta transMeta, String stepname) throws KettleValueException {
		super(parent, (BaseStepMeta)object, transMeta, stepname);
		// super(parent, SWT.BORDER);
		// TODO Auto-generated constructor stub

		input = (EhCachePluginPojoInputMeta) object;
		// value = input.getValue();
		cacheName = input.getCacheName();
		xmlURL = input.getXmlURL();
		className = input.getClassName();
		idFieldName = input.getIdFieldName();

	}

	@Override
	public String open() {
		// TODO Auto-generated method stub

		Shell parent = getParent();
		Display display = parent.getDisplay();

		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.NO_REDRAW_RESIZE | SWT.MIN | SWT.MAX);
		props.setLook( shell );
		
		
		txEhcachePath = new Text(shell, SWT.BORDER);
		txEhcachePath.setBounds(132, 38, 144, 19);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(10, 41, 116, 14);
		lblNewLabel.setText(Messages.getString("EhCachePluginPojoOutputDialog.Path"));
		
		txCacheName = new Text(shell, SWT.BORDER);
		txCacheName.setBounds(132, 63, 144, 19);
		
		Label lblCacheName = new Label(shell, SWT.NONE);
		lblCacheName.setText(Messages.getString("EhCachePluginPojoOutputDialog.CacheName"));
		lblCacheName.setBounds(10, 66, 116, 14);
		
		txObjectClass = new Text(shell, SWT.BORDER);
		txObjectClass.setBounds(132, 88, 144, 19);
		
		txUniqueID = new Text(shell, SWT.BORDER);
		txUniqueID.setBounds(132, 113, 144, 19);
		
		Label lblObjectClassName = new Label(shell, SWT.NONE);
		lblObjectClassName.setText(Messages.getString("EhCachePluginPojoOutputDialog.ObjectClassName"));
		lblObjectClassName.setBounds(10, 93, 116, 14);
		
		Label lblUniqueIdField = new Label(shell, SWT.NONE);
		lblUniqueIdField.setText(Messages.getString("EhCachePluginPojoOutputDialog.UniqueID"));
		lblUniqueIdField.setBounds(10, 118, 116, 14);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ok();
			}
		});
		btnNewButton.setBounds(159, 162, 94, 28);
		btnNewButton.setText("Save");
		
		Button btnReset = new Button(shell, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cancel();
			}
		});
		btnReset.setBounds(32, 162, 94, 28);
		btnReset.setText("Reset");
		
		Button btBulkLoad = new Button(shell, SWT.CHECK);
		btBulkLoad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useBulkLoad = ((Button)(e.widget)).getSelection();
			}
		});
		btBulkLoad.setBounds(132, 138, 93, 18);
		
		Label lblUseBulkLoad = new Label(shell, SWT.NONE);
		lblUseBulkLoad.setText(Messages.getString("EhCachePluginPojoOutputDialog.UseBulkLoad"));
		lblUseBulkLoad.setBounds(10, 142, 116, 14);
		
		Link link = new Link(shell, SWT.NONE);
		link.setBounds(132, 195, 55, 15);
		link.setText("<a href=\"https://github.com/sgrotz/PentahoEhCachePlugin/raw/master/PentahoEhcachePlugin_Instructions.pdf\">Help!</a>");
		
		txStepName = new Text(shell, SWT.BORDER);
		txStepName.setBounds(132, 10, 144, 19);
		
		Label lblStepname = new Label(shell, SWT.NONE);
		lblStepname.setText(Messages.getString("EhCachePluginPojoOutputDialog.StepName"));
		lblStepname.setBounds(10, 13, 116, 14);
		
		
		// *** END OF PASTING ***

		// Set the shell size, based upon previous time...
		setSize();

		getData();
		input.setChanged(changed);

		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch()) display.sleep();
		}
		return stepname;
	}

	@Override
	public void setRepository(Repository arg0) {
		// TODO Auto-generated method stub

	}

	// Read data from input (TextFileInputInfo)
	public void getData()
	{
		//wStepname.selectAll();
		txStepName.setText(stepname);
		txStepName.selectAll();

		logDebug("*** CacheName is: " + cacheName + " xml URL is: " + xmlURL);


		if (cacheName!=null)
		{
			txCacheName.setText(cacheName);
		}

		if (xmlURL!=null)
		{
			txEhcachePath.setText(xmlURL);
		}
		
		if (className!=null)
		{
			txObjectClass.setText(className);
		}
		
		if (idFieldName!=null)
		{
			txUniqueID.setText(idFieldName);
		}
		
		if (useBulkLoad)
		{
			btBulkLoad.setSelection(true);
		}

	}

	private void cancel()
	{
		stepname=null;
		input.setChanged(changed);
		dispose();
	}

	private void ok()
	{
		stepname = txStepName.getText(); // return value
		
		logDebug("*** New CacheName is: " + cacheName + " xml URL is: " + xmlURL);

		input.setCacheName( txCacheName.getText() );
		input.setXmlURL(txEhcachePath.getText());
		input.setClassName(txObjectClass.getText());
		input.setIdFieldName(txUniqueID.getText());
		
		// logDebug("*** Use Bulk Load API: " + btBulkLoad.getSelection());
		//input.setUseBulkApi(btBulkLoad.getSelection());

		dispose();
	}

}
