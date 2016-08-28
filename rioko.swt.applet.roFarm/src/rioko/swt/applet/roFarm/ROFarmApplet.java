package rioko.swt.applet.roFarm;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import rioko.swt.applet.basic.SWTApplet;
import rioko.swt.applet.roFarm.simulation.ROSimulation;

public class ROFarmApplet extends SWTApplet {
	
	private OptionsComposite options;
	private Button run;
	private Text result;
	
	@Override
	protected void createGUI() {
		shell.setLayout(new FillLayout());
		
		Composite parent = new Composite(shell, SWT.NONE);
		GridLayout parent_l = new GridLayout(1, true);
		parent.setLayout(parent_l);
		
		//Options composite
		options = new OptionsComposite(parent, SWT.NONE);
		GridData options_ld = new GridData(GridData.FILL_HORIZONTAL);
		options.setLayoutData(options_ld);
		
		//Button
		run = new Button(parent, SWT.PUSH);
		run.setText("Run Simulation");
		run.setFont(new Font(null, "Arial", 10, SWT.BOLD));
		
		//Result textbox
		result = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		result.setLayoutData(new GridData(GridData.FILL_BOTH));
		result.setEditable(false);
	}

	@Override
	protected void createGUIControl() {
		options.createControl();
		run.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				ROSimulation simulation = options.getROSimulation();
				try {
					simulation.run();
					
					result.setText(simulation.toString());
				} catch (Exception e) {
					result.setText(e.toString());
				}
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) { /* Do nothing */ }
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) { /* Do nothing */ }
		});
	}

	@Override
	protected void destroyGUI() {	}

	@Override
	protected String getWindowTitle() {
		return "ROFarm Simulator";
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected Iterable<Thread> setUpApplet(String[] args) {		
		return new ArrayList<>();
	}

	public static void main(String[] args) {
		(new ROFarmApplet()).run(args);
	}
}
