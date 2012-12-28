package composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class MainComposite extends Composite {

	Browser browser;
	Filterer filterer;
	Visualizer visualizer;
	
	public MainComposite(Composite arg0, int arg1) {
		super(arg0, SWT.NONE);
		setLayout(null);
		
		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(10, 10, 756, 23);
		
		ToolItem tltmHarvestUrl = new ToolItem(toolBar, SWT.NONE);
		tltmHarvestUrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		tltmHarvestUrl.setText("Start harvesting");
		
		ToolItem tltmInfo = new ToolItem(toolBar, SWT.NONE);
		tltmInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		tltmInfo.setText("Info");
		
		ToolItem tltmExit = new ToolItem(toolBar, SWT.NONE);
		tltmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.exit(0);
			}
		});
		tltmExit.setText("Exit");
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 112, 223, 2);
		
		Label label_1 = new Label(this, SWT.NONE);
		label_1.setText("Harvesters");
		label_1.setBounds(90, 91, 55, 15);
		
		List list = new List(this, SWT.BORDER | SWT.V_SCROLL);
		list.setBounds(10, 120, 223, 446);
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBounds(239, 71, 527, 495);
		
		// Setting the browser content
		TabItem tbtmBrowsing = new TabItem(tabFolder, SWT.NONE);
		tbtmBrowsing.setText("Browsing");
		browser = new Browser(tabFolder, SWT.NONE);
		tbtmBrowsing.setControl(browser);

		// Setting the filterer content
		TabItem tbtmFiltering = new TabItem(tabFolder, SWT.NONE);
		tbtmFiltering.setText("Filtering");
		filterer = new Filterer(tabFolder, SWT.NONE);
		tbtmFiltering.setControl(filterer);

		// Setting the visualizer content
		TabItem tbtmVisualization = new TabItem(tabFolder, SWT.NONE);
		tbtmVisualization.setText("Visualization");
		visualizer = new Visualizer(tabFolder, SWT.NONE);
		tbtmVisualization.setControl(visualizer);
		
	}
	
	protected Control createContents(Composite parent) {
		
		return this;
	}
}
