package composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class Visualizer extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Visualizer(Composite parent, int style) {
		super(parent, style);
		
		Label lblBrowser = new Label(this, SWT.NONE);
		lblBrowser.setBounds(10, 10, 55, 15);
		lblBrowser.setText("Visualizer");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
