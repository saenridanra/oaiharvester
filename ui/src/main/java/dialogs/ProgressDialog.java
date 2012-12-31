package dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;

public class ProgressDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	ProgressBar progressBar;
	Label status;
	
	int curr = 0;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ProgressDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public void setTitle(String title){
		setText(title);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		return result;
	}
	
	public void setProgress(int progress){
		progressBar.setSelection(progress);
	}
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 122);
		shell.setText(getText());
		
		progressBar = new ProgressBar(shell, SWT.SMOOTH);
		progressBar.setBounds(10, 42, 424, 41);
		
		Label lblCurrent = new Label(shell, SWT.NONE);
		lblCurrent.setBounds(10, 10, 55, 15);
		lblCurrent.setText("Current:");
		
		status = new Label(shell, SWT.NONE);
		status.setBounds(71, 10, 363, 15);

	}
	
	public void setStatus(String status){
		this.status.setText(status);
	}

}
