package composites;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import harvesters.Harvester;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

import api.IHarvester;
import api.IPaginatable;
import org.eclipse.wb.swt.SWTResourceManager;

public class Browser extends Composite implements IPaginatable {

	Label lblError;
	Paginator paginator;
	IHarvester harvester;

	List<BrowserItem> items;

	int maxPage = 0;
	int page = 0;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public Browser(Composite parent, int style) {
		super(parent, SWT.NONE);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		setSize(500, 850);

		items = new ArrayList<BrowserItem>();

		if (harvester == null) {
			lblError = new Label(this, SWT.NONE);
			lblError.setBounds(10, 10, 405, 15);
			lblError.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
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


			maxPage = ((Harvester) harvester).getRecords().size() / 10;
			paginator = new Paginator(this, SWT.NONE, this, maxPage);
			paginator.setBounds(0, 0, 450, 50);
		}
		if (items.size() > 0) {
			for (BrowserItem item : items) {
				item.dispose();
			}

			items.clear();
		}

		int i = 0;
		
		if(page != maxPage){
			for (int k = page * 10; k < (page * 10) + 10; k++) {
				BrowserItem item  = new BrowserItem(this, SWT.NONE, harvester,
						((Harvester) harvester).getRecords().get(k));
				item.setBounds(0, 50 + (i * 50), 450, 50);
				items.add(item);
				i++;
			}
		}
		else{
			for (int k = page * 10; k < ((Harvester) harvester).getRecords().size(); k++) {
				BrowserItem item  = new BrowserItem(this, SWT.NONE, harvester,
						((Harvester) harvester).getRecords().get(k));
				item.setBounds(0, 50 + (i * 50), 450, 50);
				items.add(item);
				i++;
			}
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
