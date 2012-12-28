package composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

public class Browser extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Browser(Composite parent, int style) {
		super(parent, style);
		
		Label lblBrowser = new Label(this, SWT.NONE);
		lblBrowser.setBounds(10, 10, 55, 15);
		lblBrowser.setText("Browser");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
