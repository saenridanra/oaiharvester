package ui.dialogs;

import java.util.Collections;

import org.dom4j.Element;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import se.kb.oai.pmh.Record;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.StyledText;

public class BrowserItemDetailDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	
	private Record record;
	private ScrolledComposite scrolledComposite;
	private StyledText styledText;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public BrowserItemDetailDialog(Shell parent, int style, Record record) {
		super(parent, style);
		setText("Details");
		
		this.record = record;
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
		shell.setSize(450, 400);
		shell.setText(getText());
		
		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnClose.setBounds(359, 336, 75, 25);
		btnClose.setText("Close");
		
		scrolledComposite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(0, 0, 444, 330);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		styledText = new StyledText(scrolledComposite, SWT.BORDER);
		scrolledComposite.setContent(styledText);
		scrolledComposite.setMinSize(styledText.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		StringBuilder output = new StringBuilder();
		
		for(Object e : record.getMetadata().elements()){
			if(e instanceof Element){
				output.append(((Element) e).getName()).append(":\n").append(((Element) e).getText()).append("\n\n");
				
			}
			else{
				output.append("Something went wrong with this element in the metadata.");
			}
		}

		styledText.setJustify(true);
		styledText.setText(output.toString());

	}
}
