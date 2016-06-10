package rioko.swt.applet.basic.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class InfoDialog extends Dialog {
	private static final Rectangle SCREEN = Display.getDefault().getBounds(); 
	
	private Shell parent = null;
	
	private Label infoText = null;
	
	protected int height = 100;
	protected int width = 300;
	
	public InfoDialog(Shell shell, int style) {
		super(new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL), style);
		
		parent = this.getParent();
		parent.setFont(new Font(null, "Arial", 11, SWT.BOLD));
		parent.setLayout(new FillLayout());
		parent.setBounds((SCREEN.width - width)/2, (SCREEN.height - height)/2, width, height);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		
		infoText = new Label(composite, SWT.NONE);
		infoText.setFont(new Font(null, "Arial", 10, SWT.NONE));
		
		GridData text_ld = new GridData(GridData.FILL_BOTH | GridData.BEGINNING);
		text_ld.horizontalIndent = 10;
		text_ld.verticalIndent = 10;
		infoText.setLayoutData(text_ld);
	}
	
	public InfoDialog(Shell shell, int style, String title) {
		this(shell, style);
		this.setTitle(title);
	}
	
	public void setTitle(String title) {
		parent.setText(title);
	}

	@Override
	public void setText(String text) {
		this.infoText.setText(text);
	}
	
	public void open() {
		parent.open();
	}
	
	public void close() {
		parent.close();
	}
	
	protected void setSize(int width, int height) {
		this.width = width;
		this.height = height;

		this.parent.setBounds((SCREEN.width - this.width)/2, (SCREEN.height - this.height)/2, this.width, this.height);
	}
	
	protected void setSize(Point point) {
		this.setSize(point.x, point.y);
	}
}
