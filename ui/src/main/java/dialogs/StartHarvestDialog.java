package dialogs;

import harvesters.Probado;

import java.util.List;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import api.IHarvester;
import api.IHarvesterObserver;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class StartHarvestDialog extends Dialog implements IHarvesterObserver {

	protected Object result;
	protected Shell shell;
	private ProgressDialog progress;

	private List<IHarvester> harvesters;

	private CCombo combo;

	private int recordsIncomeCount = 0;
	private int headersIncomeCount = 0;
	private int recordsSize = 0;
	private int headersSize = 0;
	private int recordsProgress = 0;
	private int headersProgress = 0;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public StartHarvestDialog(Shell parent, int style) {
		super(parent, style);
		setText("Select a harvester to harvest now");
	}

	public void initWithList(List<IHarvester> harvesters) {
		this.harvesters = harvesters;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 170);
		shell.setText(getText());

		combo = new CCombo(shell, SWT.BORDER);
		combo.setBounds(118, 28, 316, 21);

		combo.add("Probado", 0);

		Label lblSelectAHarvester = new Label(shell, SWT.NONE);
		lblSelectAHarvester.setBounds(10, 34, 102, 15);
		lblSelectAHarvester.setText("Select a harvester");

		Button btnHarvest = new Button(shell, SWT.NONE);
		btnHarvest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnHarvest();
			}
		});
		btnHarvest.setBounds(278, 106, 75, 25);
		btnHarvest.setText("Harvest");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnClose();
			}
		});
		btnCancel.setBounds(359, 106, 75, 25);
		btnCancel.setText("Cancel");

	}

	private void btnHarvest() {
		int selected = this.combo.getSelectionIndex();
		IHarvester harvester = null;

		switch (selected) {
		case 0:
			harvester = new Probado();
			harvester.registerObserver(this);
			
			harvesters.add(harvester);
			break;
		default:
			break;
		}

		if (harvester != null) {
			progress = new ProgressDialog(getParent(), SWT.DIALOG_TRIM);
			progress.open();
			harvester.harvestAndHold();
		} else {

		}
	}

	private void btnClose() {
		this.getParent().close();
	}

	public void dataIncome(final int count) {
		recordsIncomeCount += count;
		
		recordsProgress = (int) (((double) recordsIncomeCount / recordsSize) * 100);
		
		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				progress.setProgress(recordsProgress);
				progress.setStatus("Recieved " + recordsIncomeCount + "/" + recordsSize
						+ " records");
			}
		});

	}

	public void dataIncomeFinished() {
		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				progress.setStatus("Finished recieving records");
				progress.getParent().dispose();
			}
		});
	}

	public void headerIncome(final int count) {
		headersIncomeCount += count;
		
		headersProgress = (int) (((double) headersIncomeCount / headersSize) * 100);
		
		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				progress.setProgress(headersProgress);
				progress.setStatus("Recieved " + headersIncomeCount + "/" + headersSize
						+ " headers");
			}
		});
	}

	public void headerIncomeFinished() {
		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				progress.setStatus("Finished recieving headers");
			}
		});
	}

	public void headerSize(int size) {
		headersSize = size;
		
	}

	public void dataSize(int size) {
		recordsSize = size;
		
	}

	public List<IHarvester> getHarvesters() {
		return harvesters;
	}
	
}
