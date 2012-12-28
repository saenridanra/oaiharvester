package composites;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class MainComposite extends Composite {

	public MainComposite(Composite arg0, int arg1) {
		super(arg0, arg1);
		
		Label label = new Label(this, arg1);
		label.setText("helo!");
	}
	
	protected Control createContents(Composite parent) {
		
		return this;
	}


}
