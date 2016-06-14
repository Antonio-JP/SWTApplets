package rioko.swt.applet.console_client;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rioko.swt.applet.basic.SWTApplet;
import rioko.swt.applet.basic.text.PrintableText;

public class ConsoleApplet extends SWTApplet {
	
	private ConsoleController controller = ConsoleController.getController(this);

	private PrintableText console = null;
	
	private Text host = null, port = null;
	private Button connect = null;
	
	private Text input = null;
	private Button exit = null;

	private boolean finished;
	
	@Override
	protected void createGUI() {
		shell.setLayout(new FillLayout());
		
		Composite parent = new Composite(shell, SWT.NONE);
		GridLayout parent_l = new GridLayout(1, true);
		parent.setLayout(parent_l);
		
		//Connection Spot
		Composite connection = new Composite(parent, SWT.BORDER);
		GridLayout connection_l = new GridLayout(3, false);
		connection.setLayout(connection_l);
		
		GridData connection_ld = new GridData(GridData.FILL_HORIZONTAL);
		connection.setLayoutData(connection_ld);
		
		Composite host_c = new Composite(connection, SWT.NONE);
		host_c.setLayout(new GridLayout(2,false));
		host_c.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label host_l = new Label(host_c, SWT.NONE);
		host_l.setFont(new Font(null, "Arial", 10, SWT.BOLD));
		host_l.setText("Host:");
		
		host = new Text(host_c, SWT.BORDER);
		host.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite port_c = new Composite(connection, SWT.NONE);
		port_c.setLayout(new GridLayout(2,false));
		port_c.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label port_l = new Label(port_c, SWT.NONE);
		port_l.setFont(new Font(null, "Arial", 10, SWT.BOLD));
		port_l.setText("Port:");
		
		port = new Text(port_c, SWT.BORDER);
		port.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		connect = new Button(connection, SWT.PUSH);
		connect.setText("Disconnect");
		connect.setFont(new Font(null, "Arial", 10, SWT.BOLD));
		//Console Position
		console = new PrintableText(parent, SWT.BORDER);
		console.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		
		//Input zone
		Composite input_c = new Composite(parent, SWT.NONE);
		input_c.setLayout(new GridLayout(2, false));
		input_c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		input = new Text(input_c, SWT.BORDER);
		input.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		exit = new Button(input_c, SWT.PUSH);
		exit.setText("Exit");
		exit.setFont(new Font(null, "Arial", 10, SWT.BOLD));
	}

	@Override
	protected void createGUIControl() {
		connect.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				if(controller.isConnected()) { // Is connected case
					controller.close();
					changeToDisconnected();
				} else {
					if(controller.connect(host.getText(), port.getText())) {
						changeToConnected();
					}
				}
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		exit.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				finished = true;
				controller.close();
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		input.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(arg0.character == '\r' || arg0.character == '\n') {
					if(controller.sendMessage(input.getText() + '\n')) {
						input.setText("");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
		//Connection with enter
		KeyListener connectionListenerWithReturn = new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(arg0.character == '\r' || arg0.character == '\n') {
					if(!controller.isConnected()) {
						if(controller.connect(host.getText(), port.getText())) {
							changeToConnected();
						}
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {}
		};
		host.addKeyListener(connectionListenerWithReturn);
		port.addKeyListener(connectionListenerWithReturn);
		connect.addKeyListener(connectionListenerWithReturn);
	}

	@Override
	protected void destroyGUI() {
		ConsoleConstants.OUTPUT_CONSOLE_STREAM = System.out;
	}

	@Override
	protected String getWindowTitle() {
		return "Console Client";
	}

	@Override
	protected boolean isFinished() {
		return this.finished;
	}

	@Override
	protected Iterable<Thread> setUpApplet(String[] args) {
		this.changeToDisconnected();

		if(args.length >= 1) {
			this.host.setText(args[0]);
		}
		if(args.length >= 2) {
			this.port.setText(args[1]);
		}
		
		ConsoleConstants.OUTPUT_CONSOLE_STREAM = console.getPrintStream();
		
		return new ArrayList<>();
	}

	public static void main(String[] args) {
		(new ConsoleApplet()).run(args);
	}
	
	//GUI Control methods
	protected void changeToDisconnected() {
		connect.setText("Connect");
		host.setEditable(true);
		port.setEditable(true);
		
		input.setEditable(false);
		
		connect.setFocus();
	}


	protected void changeToConnected() {
		connect.setText("Disconnect");
		host.setEditable(false);
		port.setEditable(false);
		
		input.setEditable(true);
		
		input.setFocus();
	}
}
