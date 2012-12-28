package frames;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import composites.MainComposite;

public class ApplicationStarter extends ApplicationWindow {

	public ApplicationStarter(Shell parentShell) {
		super(parentShell);
		createActions();
		addToolBar(SWT.NONE);
		addMenuBar();
		addStatusLine();
	}

	protected Control createContents(Composite parent) {
		Composite container = new MainComposite(parent, SWT.NONE);
		return container;
	}

	private void createActions() {
	}

	protected MenuManager createMenuManager() {
		MenuManager result = new MenuManager("menu");
		return result;
	}

	protected ToolBarManager createToolBarManager(int arg) {
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.WRAP);
		return toolBarManager;
	}

	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		statusLineManager.setMessage(null, "");
		return statusLineManager;
	}

	public static void main(String args[]) {
		try {
			Display display = new Display();
			Shell shell = new Shell(display);

			// *** construct Shell children here ***
			
			ApplicationStarter starter = new ApplicationStarter(shell);
			starter.open();
			
			// process all user input events
			while(!shell.isDisposed()) {
			   // process the next event, wait when none available
			   if(!display.readAndDispatch()) {
			       display.sleep();
			   }
			}
			display.dispose();  // must always clean up
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("OAI Harvester");
	}

	protected Point getInitialSize() {
		return new Point(500, 375);
	}

}