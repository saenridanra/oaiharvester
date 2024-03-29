package ui.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import core.api.IHarvester;


public class Visualizer extends Composite {
	
	private Label lblError;
	
	private org.eclipse.swt.browser.Browser browser;
	
	IHarvester harvester;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Visualizer(Composite parent, int style) {
		super(parent, style);
		
		this.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				resize();
			}
		});
		
		if(harvester == null){
			lblError = new Label(this, SWT.NONE);
			lblError.setBounds(10, 10, 405, 15);
			lblError.setText("You have not selected a harvester yet. Please create and select one.");
		}

	}

	public IHarvester getHarvester() {
		return harvester;
	}

	public void setHarvester(IHarvester harvester) {
		this.harvester = harvester;
		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				initialize();
			}
		});
	}
	
	private void initialize() {
		if (!lblError.isDisposed()) {
			lblError.dispose();
		}
		
		browser = new Browser(this, SWT.NONE);
		browser.setSize(this.getClientArea().width, this.getClientArea().height);
		browser.setUrl(harvester.getSavePath() + "/statistics.html");

	}
	
	public void resize(){
		if(browser == null) return;
		browser.setSize(this.getClientArea().width, this.getClientArea().height);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
