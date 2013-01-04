package dialogs;

import harvesters.Harvester;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import api.IHarvester;
import api.IHarvesterObserver;

public class StartHarvestDialog extends Dialog implements IHarvesterObserver {

	protected Object result;
	protected Shell shell;
	private ProgressDialog progress;

	private List<IHarvester> harvesters;

	private int recordsIncomeCount = 0;
	private int headersIncomeCount = 0;
	private int recordsSize = 0;
	private int headersSize = 0;
	private int recordsProgress = 0;
	private int headersProgress = 0;
	private int analyzeCount = 0;
	private int analyzeProgress = 0;
	
	private Text harvesterUrl;
	private Text savePath;

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
		shell.setSize(450, 272);
		shell.setText(getText());

		Button btnHarvest = new Button(shell, SWT.NONE);
		btnHarvest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnHarvest();
			}
		});
		btnHarvest.setBounds(278, 208, 75, 25);
		btnHarvest.setText("Harvest");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnClose();
			}
		});
		btnCancel.setBounds(359, 208, 75, 25);
		btnCancel.setText("Cancel");
		
		Label lblTheUrlOf = new Label(shell, SWT.NONE);
		lblTheUrlOf.setBounds(10, 10, 171, 15);
		lblTheUrlOf.setText("The url of the harvester");
		
		harvesterUrl = new Text(shell, SWT.BORDER);
		harvesterUrl.setBounds(10, 31, 424, 21);
		
		Label lblWhereDoYou = new Label(shell, SWT.NONE);
		lblWhereDoYou.setBounds(10, 78, 424, 15);
		lblWhereDoYou.setText("Where do you want to save your harvested data?");
		
		savePath = new Text(shell, SWT.BORDER);
		savePath.setBounds(10, 99, 424, 21);
		
		Button btnChoose = new Button(shell, SWT.NONE);
		btnChoose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 DirectoryDialog  dialog = new DirectoryDialog(shell); 
				 dialog.setText("Choose a folder");
				 String platform = SWT.getPlatform();
				 String homefolder = System.getProperty("user.home");
				 dialog.setFilterPath(homefolder);
				 
				 dialog.open();
				 
				 savePath.setText(dialog.getFilterPath());
			}
		});
		btnChoose.setImage(SWTResourceManager.getImage(StartHarvestDialog.class, "/org/jdesktop/swingx/plaf/basic/resources/search.gif"));
		btnChoose.setBounds(359, 126, 75, 25);

	}

	private void btnHarvest() {
		IHarvester harvester = null;
		try {
			harvester = new Harvester(harvesterUrl.getText(), savePath.getText());
		} catch (Exception e) {
			ErrorDialog dialog = new ErrorDialog(new Shell(), SWT.DIALOG_TRIM, e.getMessage());
			dialog.open();
			return;
		}
		
		harvester.registerObserver(this);

		if (harvester != null) {
			progress = new ProgressDialog(getParent(), SWT.NONE);
			progress.open();
			harvester.harvestAndHold();
		}
		
		harvesters.add(harvester);
	}

	private void btnClose() {
		harvesters.clear();
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

	public void analayzingRecords(final int count) {
		analyzeCount += count;
		
		analyzeProgress = (int) (((double) analyzeCount / recordsSize) * 100);
		
		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				progress.setProgress(analyzeProgress);
				progress.setStatus("Analyzing " + analyzeCount + "/" + recordsSize
						+ " records");
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

	public void finishedOperation() {
		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				progress.getParent().dispose();
			}
		});
	}
}
