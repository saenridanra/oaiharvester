package composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

import api.IHarvester;

import se.kb.oai.pmh.Record;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import dialogs.BrowserItemDetailDialog;

public class BrowserItem extends Composite {

	private IHarvester harvester;
	private Record record;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public BrowserItem(Composite parent, int style, IHarvester harvester,
			final Record record) {
		super(parent, SWT.NONE);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		this.record = record;
		this.harvester = harvester;

		setSize(450, 50);

		Label lblName = new Label(this, SWT.NONE);
		lblName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblName.setBounds(10, 20, 55, 15);
		lblName.setText("Name:");

		Button btnDetails = new Button(this, SWT.NONE);
		btnDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(record != null){
					new BrowserItemDetailDialog(new Shell(), SWT.DIALOG_TRIM, record).open();
				}
			}
		});
		btnDetails.setBounds(365, 15, 75, 25);
		btnDetails.setText("Details");

		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 48, 450, 2);

		Label label_1 = new Label(this, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_1.setBounds(58, 20, 265, 15);

		try {
			label_1.setText(harvester.getMetadataValue("title", record));
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
}
