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



import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaAndData;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.dialog.EnterValueDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import com.ehcache.pentaho.*;

public class EhCachePluginDummyPojoDialog extends BaseStepDialog implements StepDialogInterface {

	private EhCachePluginDummyPojoMeta input;
	private String cacheName;
	private String xmlURL;

	private Label        wlValName;
	private Text         wValName;
	private FormData     fdlValName, fdValName;

	private Label        wlValue;
	private Button       wbValue;
	private Text         wValue;
	private FormData     fdlValue, fdbValue, fdValue;

	public EhCachePluginDummyPojoDialog(Shell parent, Object object,
			TransMeta transMeta, String stepname) throws KettleValueException {
		super(parent, (BaseStepMeta)object, transMeta, stepname);
		// TODO Auto-generated constructor stub

		input = (EhCachePluginDummyPojoMeta) object;
		// value = input.getValue();
		cacheName = input.getCacheName();
		xmlURL = input.getXmlURL();

	}

	@Override
	public String open() {
		// TODO Auto-generated method stub

		Shell parent = getParent();
		Display display = parent.getDisplay();

		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
		props.setLook( shell );
		setShellImage(shell, input);

		ModifyListener lsMod = new ModifyListener() 
		{
			public void modifyText(ModifyEvent e) 
			{
				input.setChanged();
			}
		};
		changed = input.hasChanged();


		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth  = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText(Messages.getString("EhCachePluginOutputDialog.Shell.Title")); //$NON-NLS-1$

		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;


		// Stepname line
		wlStepname=new Label(shell, SWT.RIGHT);
		wlStepname.setText(Messages.getString("EhCachePluginOutputDialog.StepName.Label")); //$NON-NLS-1$
		props.setLook( wlStepname );
		fdlStepname=new FormData();
		fdlStepname.left = new FormAttachment(0, 0);
		fdlStepname.right= new FormAttachment(middle, -margin);
		fdlStepname.top  = new FormAttachment(0, margin);
		wlStepname.setLayoutData(fdlStepname);
		wStepname=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wStepname.setText(stepname);
		props.setLook( wStepname );
		wStepname.addModifyListener(lsMod);
		fdStepname=new FormData();
		fdStepname.left = new FormAttachment(middle, 0);
		fdStepname.top  = new FormAttachment(0, margin);
		fdStepname.right= new FormAttachment(100, 0);
		wStepname.setLayoutData(fdStepname);

		// CacheConfiguration URL
		wlValName=new Label(shell, SWT.RIGHT);
		wlValName.setText(Messages.getString("EhCachePluginOutputDialog.Ehcache.Label")); //$NON-NLS-1$
		props.setLook( wlValName );
		fdlValName=new FormData();
		fdlValName.left = new FormAttachment(0, 0);
		fdlValName.right= new FormAttachment(middle, -margin);
		fdlValName.top  = new FormAttachment(wStepname, margin);
		wlValName.setLayoutData(fdlValName);
		wValName=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook( wValName );
		wValName.addModifyListener(lsMod);
		fdValName=new FormData();
		fdValName.left = new FormAttachment(middle, 0);
		fdValName.right= new FormAttachment(100, 0);
		fdValName.top  = new FormAttachment(wStepname, margin);
		wValName.setLayoutData(fdValName);

		// Cache Name
		wlValue=new Label(shell, SWT.RIGHT);
		wlValue.setText(Messages.getString("EhCachePluginOutputDialog.CacheName.Label")); //$NON-NLS-1$
		props.setLook( wlValue );
		fdlValue=new FormData();
		fdlValue.left = new FormAttachment(0, 0);
		fdlValue.right= new FormAttachment(middle, -margin);
		fdlValue.top  = new FormAttachment(wValName, margin);
		wlValue.setLayoutData(fdlValue);

		wValue=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook( wValue );
		wValue.addModifyListener(lsMod);
		fdValue=new FormData();
		fdValue.left = new FormAttachment(middle, 0);
		fdValue.right= new FormAttachment(100, 0);
		fdValue.top  = new FormAttachment(wlValue, margin);
		wValue.setLayoutData(fdValue);

		// Some buttons
		wOK=new Button(shell, SWT.PUSH);
		wOK.setText(Messages.getString("System.Button.OK")); //$NON-NLS-1$
		wCancel=new Button(shell, SWT.PUSH);
		wCancel.setText(Messages.getString("System.Button.Cancel")); //$NON-NLS-1$

		BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel}, margin, wValue);

		// Add listeners
		lsCancel   = new Listener() { 

			@Override
			public void handleEvent(Event arg0) {
				cancel();

			} 
		};

		lsOK       = new Listener() { 
			public void handleEvent(Event e) { 
				ok();     
			}
		};

		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener    (SWT.Selection, lsOK    );

		lsDef=new SelectionAdapter() { 
			public void widgetDefaultSelected(SelectionEvent e) { 
				ok(); 
			} 
		};

		wStepname.addSelectionListener( lsDef );
		wValName.addSelectionListener( lsDef );

		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(	new ShellAdapter() { 
			public void shellClosed(ShellEvent e) { 
				cancel(); 
			} 
		} );

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
		wStepname.selectAll();

			logDebug("*** CacheName is: " + cacheName + " xml URL is: " + xmlURL);


			if (cacheName!=null)
			{
				wValue.setText(cacheName);
			}

			if (xmlURL!=null)
			{
				wValName.setText(xmlURL);
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
		stepname = wStepname.getText(); // return value
		
		logDebug("*** New CacheName is: " + cacheName + " xml URL is: " + xmlURL);

		input.setCacheName( wValue.getText() );
		input.setXmlURL(wValName.getText());

		dispose();
	}

}
