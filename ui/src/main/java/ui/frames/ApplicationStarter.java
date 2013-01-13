package ui.frames;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import ui.composites.MainComposite;

public class ApplicationStarter extends ApplicationWindow {
	
	private Shell shell;
	private Composite container;
	
	public ApplicationStarter(Shell parentShell) {
		super(parentShell);
		shell = parentShell;
		createActions();
		addToolBar(SWT.NONE);
		addMenuBar();
		addStatusLine();
	}

	protected Control createContents(Composite parent) {
		container = new MainComposite(parent, SWT.NONE);
		container.addListener(SWT.Resize, new Listener() {
			
			public void handleEvent(Event event) {
				resize();
			}
		});
		
		
		return container;
	}

	private void createActions() {
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
	
	public void resize(){
		container.setSize(this.getShell().getClientArea().width, this.getShell().getClientArea().height);
	}

	protected Point getInitialSize() {
		return new Point(800, 700);
	}
}