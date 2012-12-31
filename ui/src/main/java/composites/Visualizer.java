package composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import api.IHarvester;

public class Visualizer extends Composite {
	
	IHarvester harvester;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Visualizer(Composite parent, int style) {
		super(parent, style);
		
		if(harvester == null){
			Label lblError = new Label(this, SWT.NONE);
			lblError.setBounds(10, 10, 405, 15);
			lblError.setText("You have not selected a harvester yet. Please create and select one.");
		}

	}

	public IHarvester getHarvester() {
		return harvester;
	}

	public void setHarvester(IHarvester harvester) {
		this.harvester = harvester;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
