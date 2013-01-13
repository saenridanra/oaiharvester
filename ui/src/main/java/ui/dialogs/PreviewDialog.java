package ui.dialogs;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;

public class PreviewDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Button btnClose;
	
	private String url;
	private Label label;
	private ScrolledComposite scrolledComposite;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PreviewDialog(Shell parent, int style, String url) {
		super(parent, style);
		
		this.url = url;
		setText("Preview image");
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
		shell.setSize(624, 632);
		shell.setText(getText());
		
		btnClose = new Button(shell, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				doBtnCloseWidgetSelected(e);
			}
		});
		
		scrolledComposite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(0, 0, shell.getBounds().width, shell.getBounds().height-100);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		label = new Label(scrolledComposite, SWT.NONE);
		
		try {
			Image image = ImageDescriptor.createFromURL(new URL(url)).createImage();
			
			label.setSize(image.getBounds().width, image.getBounds().height);
			label.setImage(image);
			
			btnClose.setBounds(292, 568, 75, 25);
			btnClose.setText("Close");
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		scrolledComposite.setContent(label);
		scrolledComposite.setMinSize(label.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	protected void doBtnCloseWidgetSelected(final SelectionEvent e) {
		getParent().dispose();
	}
}
