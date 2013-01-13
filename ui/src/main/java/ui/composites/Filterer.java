package ui.composites;

import java.util.ArrayList;
import java.util.List;


import org.dom4j.Element;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import core.api.IHarvester;
import core.harvesters.Harvester;

import se.kb.oai.pmh.Record;
import ui.api.IPaginatable;
import ui.api.ISWTCallback;


public class Filterer extends Composite implements IPaginatable, ISWTCallback {

	private Paginator paginator;
	private SearchPartial searchPartial;
	private IHarvester harvester;
	private Label lblError;

	private List<Record> searchedRecords;

	List<BrowserItem> items;

	int maxPage = 0;
	int page = 0;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public Filterer(Composite parent, int style) {
		super(parent, style);
		
		setSize(500, 950);

		searchedRecords = new ArrayList<Record>();
		items = new ArrayList<BrowserItem>();

		if (harvester == null) {
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

			searchPartial = new SearchPartial(this, SWT.NONE, this);
			searchPartial.setBounds(0, 0, 450, 50);

			maxPage = searchedRecords.size() / 10;
			paginator = new Paginator(this, SWT.NONE, this, maxPage);
			paginator.setBounds(0, 60, 450, 50);
		}
		if (items.size() > 0) {
			for (BrowserItem item : items) {
				item.dispose();
			}

			items.clear();
		}

		int i = 0;

		System.out.println("Page: " + page + " MaxPage: " + maxPage);
		if (page != maxPage) {
			for (int k = page * 10; k < (page * 10) + 10; k++) {
				BrowserItem item = new BrowserItem(this, SWT.NONE, harvester,
						searchedRecords.get(k));
				item.setBounds(0, 120 + (i * 50), 450, 50);
				items.add(item);
				i++;
			}
		} else {
			for (int k = page * 10; k < searchedRecords.size(); k++) {
				BrowserItem item = new BrowserItem(this, SWT.NONE, harvester,
						searchedRecords.get(k));
				item.setBounds(0, 120 + (i * 50), 450, 50);
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

	public void callback(Class clazz) {
		System.out.println("Executing callback");
		if (clazz == SearchPartial.class) {
			performSearch();
		}

	}

	private void performSearch() {
		String searchString = searchPartial.getSearchString().toLowerCase();
		searchedRecords.clear();
		searchedRecords.addAll(((Harvester) harvester).getRecords());

		int i = 0;
		
		for (Record record : ((Harvester) harvester).getRecords()) {
			boolean contained = false;
			for (Object e : record.getMetadata().elements()) {
				if (((Element) e).getText().toLowerCase().contains(searchString)) {
					contained = true;
				}
			}

			if (!contained) {
				searchedRecords.remove(i);
				i--;
			}

			i++;
		}

		page = 0;
		
		if(paginator != null){
			maxPage = searchedRecords.size() / 10;
			paginator.setMaxPage(maxPage);
		}
		

		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				initialize();
			}
		});

	}
}
