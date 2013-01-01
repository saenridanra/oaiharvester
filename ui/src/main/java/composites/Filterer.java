package composites;

import java.util.List;

import harvesters.Probado;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import api.IHarvester;
import api.IPaginatable;

public class Filterer extends Composite implements IPaginatable{
	

	Paginator paginator;
	private IHarvester harvester;
	private Label lblError;

	List<BrowserItem> items;

	int page = 0;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Filterer(Composite parent, int style) {
		super(parent, style);
		
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
			
			paginator = new Paginator(this, SWT.NONE, this, ((Probado) harvester).getRecords().size() / 10);
			paginator.setBounds(0, 0, 450, 50);
		}
		if (items.size() > 0) {
			for (BrowserItem item : items) {
				item.dispose();
			}

			items.clear();
		}

		int i = 0;
		for (int k = page * 10; k < (page * 10) + 10; k++) {
			BrowserItem item  = new BrowserItem(this, SWT.NONE, harvester,
					((Probado) harvester).getRecords().get(k));
			item.setBounds(0, 50 + (i * 50), 450, 50);
			items.add(item);
			i++;
		}

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void next() {
		page++;
		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				initialize();
			}
		});
	}

	public void prev() {
		page--;
		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				initialize();
			}
		});
	}

}
