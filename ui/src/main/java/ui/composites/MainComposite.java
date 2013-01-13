package ui.composites;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.swt.widgets.Menu;

import core.api.IHarvester;

import ui.dialogs.ErrorDialog;
import ui.dialogs.InfoDialog;
import ui.dialogs.StartHarvestDialog;
import ui.dialogs.SuccessfulHarvest;

public class MainComposite extends Composite {

	private Composite parent;
	
	private java.util.List<IHarvester> harvesters;
	
	private TabFolder tabFolder;

	private Browser browser;
	private ScrolledComposite browserScroller;
	private Filterer filterer;
	private ScrolledComposite filtererScroller;
	private Visualizer visualizer;

	private List harvesterUiList;

	public MainComposite(Composite arg0, int arg1) {
		super(arg0, SWT.NONE);
		parent = arg0;

		initialize();

		setLayout(null);
		
		this.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				tabFolder.setSize(parent.getShell().getClientArea().width - 255, parent.getShell().getClientArea().height - 90);
				browser.setSize(tabFolder.getClientArea().width-30, tabFolder.getClientArea().height-30);
				filterer.setSize(tabFolder.getClientArea().width-30, tabFolder.getClientArea().height-30);
				visualizer.setSize(tabFolder.getClientArea().width-30, tabFolder.getClientArea().height-30);
				browserScroller.setMinSize(tabFolder.getClientArea().width, browser.getSize().y);
				filtererScroller.setMinSize(tabFolder.getClientArea().width, filterer.getSize().y);
			}
		});

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

		Label label_1 = new Label(this, SWT.CENTER);
		label_1.setText("Harvesters");
		label_1.setBounds(10, 91, 223, 15);

		harvesterUiList = new List(this, SWT.BORDER | SWT.V_SCROLL);
		harvesterUiList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				updateTabs(harvesterUiList.getSelectionIndex());
			}
		});
		harvesterUiList.setBounds(10, 120, 225, 446);

		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayout(new FillLayout());
		tabFolder.setLocation(235, 70);
		tabFolder.setSize(this.getClientArea().width - 255, this.getClientArea().height - 90);

		// Setting the browser content
		TabItem tbtmBrowsing = new TabItem(tabFolder, SWT.NONE);
		tbtmBrowsing.setText("Browsing");
		browserScroller = new ScrolledComposite(tabFolder, SWT.H_SCROLL
		        | SWT.V_SCROLL);
		browserScroller.setMinSize(tabFolder.getClientArea().width, tabFolder.getClientArea().height);
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
		filtererScroller.setMinSize(tabFolder.getClientArea().width, tabFolder.getClientArea().height);
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
