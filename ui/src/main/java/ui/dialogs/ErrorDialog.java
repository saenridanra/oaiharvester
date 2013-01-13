package ui.dialogs;

import org.eclipse.osgi.internal.resolver.ComputeNodeOrder;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;

public class ErrorDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private String message;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ErrorDialog(Shell parent, int style, String message) {
		super(parent, style);
		this.message = message;
		setText("Error");
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
		shell.setText(getText());
		
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.marginHeight = 10;
		rowLayout.marginWidth = 10;
		rowLayout.spacing = 5;
		
		shell.setLayout(rowLayout);
		
		Label lblMessage = new Label(shell, SWT.WRAP);
		lblMessage.setText(message);
		
		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		btnClose.setBounds(359, 236, 75, 25);
		btnClose.setText("Close");

		shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}
	
	private void close(){
		this.getParent().close();
	}
}
