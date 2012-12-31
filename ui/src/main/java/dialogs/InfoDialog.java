package dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class InfoDialog extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public InfoDialog(Shell parent, int style) {
		super(parent, style);
		setText("Info");
	}

	/**
	 * Open the dialog.
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
		shell.setSize(450, 220);
		shell.setText(getText());
		
		CLabel lblOaiHarvester = new CLabel(shell, SWT.NONE);
		lblOaiHarvester.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));
		lblOaiHarvester.setBounds(169, 0, 112, 38);
		lblOaiHarvester.setText("OAI Harvester");
		
		Label lblVersion = new Label(shell, SWT.NONE);
		lblVersion.setBounds(119, 65, 55, 15);
		lblVersion.setText("version:");
		
		Label lblContributors = new Label(shell, SWT.NONE);
		lblContributors.setBounds(119, 86, 79, 15);
		lblContributors.setText("contributors:");
		
		Label lblsnapshot = new Label(shell, SWT.NONE);
		lblsnapshot.setBounds(199, 65, 90, 15);
		lblsnapshot.setText("0.0.1-SNAPSHOT");
		
		Label lblAndreasRain = new Label(shell, SWT.NONE);
		lblAndreasRain.setBounds(199, 86, 82, 15);
		lblAndreasRain.setText("Andreas Rain");
		
		Label lblHarrySchilling = new Label(shell, SWT.NONE);
		lblHarrySchilling.setBounds(199, 107, 82, 15);
		lblHarrySchilling.setText("Harry Schilling");
		
		Label label_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(79, 46, 299, 2);
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(79, 140, 299, 2);
		
		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnClose();
			}
		});
		btnClose.setBounds(169, 156, 112, 25);
		btnClose.setText("Close");
		
		

	}
	
	private void btnClose(){
		this.getParent().close();
	}
}
