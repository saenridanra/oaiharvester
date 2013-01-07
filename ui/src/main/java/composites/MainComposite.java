package composites;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import api.IHarvester;
import dialogs.ErrorDialog;
import dialogs.InfoDialog;
import dialogs.StartHarvestDialog;
import dialogs.SuccessfulHarvest;
import org.eclipse.swt.widgets.Menu;

public class MainComposite extends Composite {

	private java.util.List<IHarvester> harvesters;

	private Browser browser;
	private ScrolledComposite browserScroller;
	private Filterer filterer;
	private ScrolledComposite filtererScroller;
	private Visualizer visualizer;

	private List harvesterUiList;

	public MainComposite(Composite arg0, int arg1) {
		super(arg0, SWT.NONE);

		initialize();

		setLayout(null);

		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(10, 10, 756, 23);

		ToolItem tltmHarvestUrl = new ToolItem(toolBar, SWT.NONE);
		tltmHarvestUrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				startHarvesting();
			}
		});
		tltmHarvestUrl.setText("Start harvesting");

		ToolItem tltmInfo = new ToolItem(toolBar, SWT.NONE);
		tltmInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showInfo();
			}
		});
		tltmInfo.setText("Info");

		ToolItem tltmExit = new ToolItem(toolBar, SWT.NONE);
		tltmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (save()) {
					System.exit(0);
				}
			}
		});
		tltmExit.setText("Exit");

		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 112, 223, 2);

		Label label_1 = new Label(this, SWT.NONE);
		label_1.setText("Harvesters");
		label_1.setBounds(90, 91, 55, 15);

		harvesterUiList = new List(this, SWT.BORDER | SWT.V_SCROLL);
		harvesterUiList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				updateTabs(harvesterUiList.getSelectionIndex());
			}
		});
		harvesterUiList.setBounds(10, 120, 223, 446);

		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayout(new FillLayout());
		tabFolder.setBounds(239, 71, 527, 495);

		// Setting the browser content
		TabItem tbtmBrowsing = new TabItem(tabFolder, SWT.NONE);
		tbtmBrowsing.setText("Browsing");
		browserScroller = new ScrolledComposite(tabFolder, SWT.H_SCROLL
		        | SWT.V_SCROLL);
		browserScroller.setMinSize(500, 700);
		browser = new Browser(browserScroller, SWT.NONE);
		browserScroller.setContent(browser);
		browserScroller.setExpandVertical(true);
		browserScroller.setExpandHorizontal(true);
		tbtmBrowsing.setControl(browserScroller);

		// Setting the filterer content
		TabItem tbtmFiltering = new TabItem(tabFolder, SWT.NONE);
		tbtmFiltering.setText("Filtering");
		filtererScroller = new ScrolledComposite(tabFolder, SWT.H_SCROLL
		        | SWT.V_SCROLL);
		filtererScroller.setMinSize(500, 950);
		filterer = new Filterer(filtererScroller, SWT.NONE);
		filtererScroller.setContent(filterer);
		filtererScroller.setExpandVertical(true);
		filtererScroller.setExpandHorizontal(true);
		tbtmFiltering.setControl(filtererScroller);

		// Setting the visualizer content
		TabItem tbtmVisualization = new TabItem(tabFolder, SWT.NONE);
		tbtmVisualization.setText("Visualization");
		visualizer = new Visualizer(tabFolder, SWT.NONE);
		tbtmVisualization.setControl(visualizer);

	}

	private void initialize() {
		harvesters = new ArrayList<IHarvester>();
	}

	private void startHarvesting() {
		final StartHarvestDialog i = new StartHarvestDialog(new Shell(),
				SWT.DIALOG_TRIM);
		i.initWithList(harvesters);
		i.open();

		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				while (!i.getParent().isDisposed()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				harvesters = i.getHarvesters();
				finishedHarvesting();
			}
		});
	}

	private void showInfo() {
		InfoDialog i = new InfoDialog(new Shell(), SWT.DIALOG_TRIM);
		i.open();
	}

	private boolean save() {
		for(IHarvester harvester : harvesters){
			try {
				harvester.save();
			} catch (IOException e) {
				new ErrorDialog(new Shell(), SWT.DIALOG_TRIM, e.getMessage()).open();
			}
		}
		return true;
	}

	private void finishedHarvesting() {
		SuccessfulHarvest i = new SuccessfulHarvest(new Shell(),
				SWT.DIALOG_TRIM);
		i.open();
		
		save();

		updateList();
	}

	private void updateList() {
		for (int i = 0; i < harvesters.size(); i++) {
			harvesterUiList.add(harvesters.get(i).getUrl(), i);
		}
	}

	private void updateTabs(final int selectionIndex) {
		getParent().getDisplay().asyncExec(new Runnable() {
			public void run() {
				browser.setHarvester(harvesters.get(selectionIndex));
				filterer.setHarvester(harvesters.get(selectionIndex));
				visualizer.setHarvester(harvesters.get(selectionIndex));
			}
		});
	}

	protected Control createContents(Composite parent) {
		return this;
	}
}
