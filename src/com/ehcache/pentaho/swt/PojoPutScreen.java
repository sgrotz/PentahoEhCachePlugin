package com.ehcache.pentaho.swt;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Link;

public class PojoPutScreen extends Composite {
	private Text txEhcachePath;
	private Text txCacheName;
	private Text txObjectClass;
	private Text txUniqueID;
	private Text txStepName;

	private boolean useBulkApi;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PojoPutScreen(Composite parent, int style) {
		super(parent, SWT.BORDER | SWT.NO_REDRAW_RESIZE);
			
		txEhcachePath = new Text(this, SWT.BORDER);
		txEhcachePath.setBounds(132, 38, 144, 19);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBounds(10, 41, 116, 14);
		lblNewLabel.setText("Path to Ehcache.xml:");
		
		txCacheName = new Text(this, SWT.BORDER);
		txCacheName.setBounds(132, 63, 144, 19);
		
		Label lblCacheName = new Label(this, SWT.NONE);
		lblCacheName.setText("Cache Name:");
		lblCacheName.setBounds(10, 66, 116, 14);
		
		txObjectClass = new Text(this, SWT.BORDER);
		txObjectClass.setBounds(132, 88, 144, 19);
		
		txUniqueID = new Text(this, SWT.BORDER);
		txUniqueID.setBounds(132, 113, 144, 19);
		
		Label lblObjectClassName = new Label(this, SWT.NONE);
		lblObjectClassName.setText("Object Class Name:");
		lblObjectClassName.setBounds(10, 93, 116, 14);
		
		Label lblUniqueIdField = new Label(this, SWT.NONE);
		lblUniqueIdField.setText("Unique ID Field Name:");
		lblUniqueIdField.setBounds(10, 118, 116, 14);
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//ok();
			}
		});
		btnNewButton.setBounds(159, 162, 94, 28);
		btnNewButton.setText("Save");
		
		Button btnReset = new Button(this, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// cancel();
			}
		});
		btnReset.setBounds(32, 162, 94, 28);
		btnReset.setText("Reset");
		
		Button btBulkLoad = new Button(this, SWT.CHECK);
		btBulkLoad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useBulkApi = ((Button)(e.widget)).getSelection();
			}
		});
		btBulkLoad.setBounds(132, 138, 93, 18);
		
		Label lblUseBulkLoad = new Label(this, SWT.NONE);
		lblUseBulkLoad.setText("Use Bulk Load API:");
		lblUseBulkLoad.setBounds(10, 142, 116, 14);
		
		Link link = new Link(this, SWT.NONE);
		link.setBounds(132, 195, 55, 15);
		link.setText("<a href=\"https://github.com/sgrotz/PentahoEhCachePlugin/raw/master/PentahoEhcachePlugin_Instructions.pdf\">Help!</a>");
		
		txStepName = new Text(this, SWT.BORDER);
		txStepName.setBounds(132, 10, 144, 19);
		
		Label lblStepname = new Label(this, SWT.NONE);
		lblStepname.setText("Stepname:");
		lblStepname.setBounds(10, 13, 116, 14);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
