package composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class Filterer extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Filterer(Composite parent, int style) {
		super(parent, style);
		
		Label lblBrowser = new Label(this, SWT.NONE);
		lblBrowser.setBounds(10, 10, 55, 15);
		lblBrowser.setText("Filterer");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
