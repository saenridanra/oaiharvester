package ui.composites;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import ui.api.ISWTCallback;

public class SearchPartial extends Composite {
	private Text text;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SearchPartial(Composite parent, int style, final ISWTCallback callback) {
		super(parent, style);
		setSize(450, 50);
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(10, 17, 338, 21);
		
		Button btnSearch = new Button(this, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				callback.callback(SearchPartial.class);
			}
		});
		btnSearch.setBounds(365, 15, 75, 25);
		btnSearch.setText("Search");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public String getSearchString(){
		return text.getText();
	}
}
