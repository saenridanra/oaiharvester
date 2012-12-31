package dialogs;

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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

public class BrowserItemDetailDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	
	private Record record;

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
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 10, 424, 320);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		RowLayout rowLayout = new RowLayout();
		rowLayout.wrap = true;
		rowLayout.pack = false;
		rowLayout.type = SWT.VERTICAL;
		rowLayout.marginLeft = 5;
		rowLayout.marginTop = 5;
		rowLayout.marginRight = 5;
		rowLayout.marginBottom = 5;
		rowLayout.spacing = 5;
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(rowLayout);
		scrolledComposite.setContent(composite);
		
		for(Object e : record.getMetadata().elements()){
			if(e instanceof Element){
				int anzSpaces = 20 - ((Element) e).getName().length();
				
				String spaces = "";
				for(int i = 0; i < anzSpaces; i++){
					spaces += " ";
				}
				
				String content = ((Element) e).getName() + ":" + spaces + ((Element) e).getText();
				
				Label l = new Label(composite, SWT.NONE);
				l.setText(content);
				l.setSize(410, 15);
			}
			else{
				Label l = new Label(composite, SWT.NONE);
				l.setText("Something went wrong with this element in the metadata.");
				l.setSize(410, 15);
			}
		}

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}
}
