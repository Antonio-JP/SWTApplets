package rioko.swt.applet.server;

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
import org.eclipse.swt.widgets.Label;

import rioko.swt.applet.basic.SWTApplet;
import rioko.swt.applet.basic.text.PrintableText;

public class ServerApplet extends SWTApplet {

	private static ServerController controller = new ServerController();
	
	private PrintableText inputText = null;
	private PrintableText outputText = null;
	private Button exitButton = null;

	@Override
	protected String getWindowTitle() {
		return "Server App";
	}

	@Override
	protected boolean isFinished() {
		return controller.getEnded();
	}

	@Override
	protected void createGUI() {
		shell.setLayout(new FillLayout());
		
		Composite parent = new Composite(shell, SWT.NONE);
		GridLayout parentLayout = new GridLayout(2, true);
		parent.setLayout(parentLayout);
		
		//View creation
			//Input window
			Composite inputComposite = new Composite(parent, SWT.BORDER);
			GridData inputComposite_ld = new GridData(
					GridData.FILL_BOTH | 
					GridData.VERTICAL_ALIGN_CENTER | 
					GridData.HORIZONTAL_ALIGN_CENTER);
			inputComposite.setLayoutData(inputComposite_ld);
			
			GridLayout inputComposite_l = new GridLayout(1,true);
			inputComposite.setLayout(inputComposite_l);
			
				//Input Label
				Label inputLabel = new Label(inputComposite, SWT.NONE);
				GridData inputLabel_ld = new GridData(
						GridData.HORIZONTAL_ALIGN_BEGINNING | 
						GridData.FILL_HORIZONTAL);
				inputLabel.setLayoutData(inputLabel_ld);
				
				inputLabel.setText("Input: ");
				inputLabel.setFont(new Font(null, "Arial", 16, SWT.BOLD));
				//END OF: Input Label
				
				//Input Log
				inputText = new PrintableText(inputComposite, SWT.BORDER);
				GridData inputText_ld = new GridData(GridData.FILL_BOTH);
				inputText.setLayoutData(inputText_ld);
				//END OF: Input Log
			//END OF: Input Window
			//Output Window
			Composite outputComposite = new Composite(parent, SWT.BORDER);
			GridData outputComposite_ld = new GridData(
					GridData.FILL_BOTH | 
					GridData.VERTICAL_ALIGN_CENTER | 
					GridData.HORIZONTAL_ALIGN_CENTER);
			outputComposite.setLayoutData(outputComposite_ld);
			
			GridLayout outputComposite_l = new GridLayout(1,true);
			outputComposite.setLayout(outputComposite_l);
			
				//Output Label
				Label outputLabel = new Label(outputComposite, SWT.NONE);
				GridData outputLabel_ld = new GridData(
						GridData.HORIZONTAL_ALIGN_BEGINNING | 
						GridData.FILL_HORIZONTAL);
				outputLabel.setLayoutData(outputLabel_ld);
				
				outputLabel.setText("Output: ");
				outputLabel.setFont(new Font(null, "Arial", 16, SWT.BOLD));
				//END OF: Output Label
				
				//Output Log
				outputText = new PrintableText(outputComposite, SWT.BORDER);
				GridData outputText_ld = new GridData(GridData.FILL_BOTH);
				outputText.setLayoutData(outputText_ld);
				//END OF: Output Log
			//END OF: Output Window
				
			//Exit Button
			Composite buttonComposite = new Composite(parent, SWT.BORDER);
			GridData buttonComposite_ld = new GridData(
					GridData.FILL_HORIZONTAL | 
					GridData.HORIZONTAL_ALIGN_CENTER);
			buttonComposite_ld.horizontalSpan = 2;
			buttonComposite.setLayoutData(buttonComposite_ld);
			
			GridLayout buttonComposite_l = new GridLayout(1, true);
			buttonComposite.setLayout(buttonComposite_l);
			
			exitButton = new Button(buttonComposite, SWT.PUSH);
			GridData button_ld = new GridData(GridData.END);
			exitButton.setLayoutData(button_ld);
			
			exitButton.setText("Exit");
			exitButton.setFont(new Font(null, "Arial", 16, SWT.BOLD));
			//END OF: Exit Button
		//END OF: View Creation
	}
	
	@Override
	protected void createGUIControl() {
		//Control creation
		exitButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				controller.setEnded(true);
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});

		ServerConstants.INPUT_CONSOLE_STREAM = inputText.getPrintStream();
		ServerConstants.OUTPUT_CONSOLE_STREAM = outputText.getPrintStream();
	}

	@Override
	protected void destroyGUI() {
		ServerConstants.INPUT_CONSOLE_STREAM = System.out;
		ServerConstants.OUTPUT_CONSOLE_STREAM = System.out;
	}
	
	@Override
	protected Iterable<Thread> setUpApplet(String[] args) {
		ArrayList<Thread> threads = new ArrayList<>();
		Integer inPort = null, outPort = null;
		if(args.length >= 1) {
			try {
				inPort = Integer.parseInt(args[0]);
			} catch(NumberFormatException e) {
				//Do nothing -- Default input port
			}
		}
		if(args.length >= 2) {
			try {
				outPort = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {
				//Do nothing -- Default input port
			}
		}
		
		controller.setInPort((inPort == null) ? ServerConstants.DEFAULT_INPUT_PORT : inPort);
		controller.setOutPort((outPort == null) ? ServerConstants.DEFAULT_OUTPUT_PORT : outPort);
		
		Thread controllerThread = (new Thread() {
			@Override
			public void run() {
				controller.run();
			};
		});
		
		controllerThread.start();
		threads.add(controllerThread);
		
		return threads;
	}
	
	public static void main(String[] args) {
		(new ServerApplet()).run(args);
	}
}
