package composites;

import org.eclipse.swt.widgets.Composite;

import api.IPaginatable;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class Paginator extends Composite {

	private IPaginatable paginatable;
	private int maxPage, currPage = 0;

	Label pages;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public Paginator(Composite parent, int style, IPaginatable paginatable,
			int maxPage) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		this.paginatable = paginatable;
		this.maxPage = maxPage;
		
		setSize(450, 50);
		
		initialize();

	}

	private void initialize() {
		Button prev = new Button(this, SWT.NONE);
		prev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (currPage > 0) {
					currPage--;
					pages.setText("Page " + (currPage + 1) + " of " + (maxPage + 1));
					paginatable.prev();
				}
			}
		});
		prev.setBounds(10, 10, 75, 25);
		prev.setText("<");

		pages = new Label(this, SWT.NONE);
		pages.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		pages.setBounds(181, 15, 177, 15);
		pages.setText("Page " + currPage + " of " + maxPage);

		Button next = new Button(this, SWT.NONE);
		next.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (currPage < maxPage) {
					currPage++;
					pages.setText("Page " + (currPage + 1) + " of " + (maxPage + 1));
					paginatable.next();
				}
			}
		});
		next.setText(">");
		next.setBounds(364, 10, 75, 25);

		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(0, 41, 450, 2);
	}

	@Override
	protected void checkSubclass() {

	}
	
	public void setMaxPage(int maxPage){
		this.currPage = 0;
		this.maxPage = maxPage;
		pages.setText("Page " + (currPage + 1) + " of " + (this.maxPage + 1));
		
	}
}
