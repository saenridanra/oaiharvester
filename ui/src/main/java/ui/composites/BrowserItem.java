package ui.composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;


import se.kb.oai.pmh.Record;
import ui.dialogs.BrowserItemDetailDialog;
import ui.dialogs.PreviewDialog;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import core.api.IHarvester;


public class BrowserItem extends Composite {

	private IHarvester harvester;
	private Record record;
	private Button btnPreview;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public BrowserItem(Composite parent, int style, final IHarvester harvester,
			final Record record) {
		super(parent, SWT.NONE);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		this.record = record;
		this.harvester = harvester;

		setSize(450, 50);

		Label lblName = new Label(this, SWT.NONE);
		lblName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblName.setBounds(10, 20, 47, 15);
		lblName.setText("Name:");

		Button btnDetails = new Button(this, SWT.NONE);
		btnDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(record != null){
					new BrowserItemDetailDialog(new Shell(), SWT.DIALOG_TRIM, record, harvester).open();
				}
			}
		});
		btnDetails.setBounds(365, 15, 75, 25);
		btnDetails.setText("Details");

		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 48, 450, 2);

		Label label_1 = new Label(this, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_1.setBounds(72, 20, 183, 15);
		
		btnPreview = new Button(this, SWT.NONE);
		btnPreview.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				doBtnPreviewWidgetSelected(e);
			}
		});
		btnPreview.setBounds(284, 15, 75, 25);
		btnPreview.setText("Preview");
		
		try {
			if(harvester.getMetadataFormat().equals("dc")){
				label_1.setText(harvester.getMetadataValue("title", record));
				btnPreview.setEnabled(false);
			}
			else if(harvester.getMetadataFormat().equals("p3dm")){
				label_1.setText(harvester.getElement("TITLE", record).getText());
				btnPreview.setEnabled(true);
			}
		} catch (Exception e) {

		}

	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	protected void doBtnPreviewWidgetSelected(final SelectionEvent e) {
		if(btnPreview.isEnabled()){
			String preview = harvester.getElement("PREVIEWS", record)
					.element("PREVIEW_JPG").getText();
			
			PreviewDialog d = new PreviewDialog(new Shell(), SWT.DIALOG_TRIM, preview);
			d.open();
		}
	}
}
