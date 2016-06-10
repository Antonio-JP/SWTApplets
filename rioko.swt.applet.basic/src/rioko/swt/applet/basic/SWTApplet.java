package rioko.swt.applet.basic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import rioko.swt.applet.basic.dialog.InfoDialog;

public abstract class SWTApplet{

	protected Display display;
	protected Shell shell;
	
	public SWTApplet() {
		super();
	}
	
	protected SWTApplet(Display display, Shell shell) {
		this.display = display;
		this.shell = shell;
	}
	
	protected Display getDisplay() {
		return display;
	}
	
	public Shell getShell() {
		return shell;
	}
	
	abstract protected void createGUI();

	protected abstract void createGUIControl();
	
	abstract protected void destroyGUI();
	
	protected abstract Iterable<Thread> setUpApplet(String[] args);

	protected abstract String getWindowTitle();

	protected void doWhileWaiting(Display display) {
		display.sleep();
	}
	
	protected abstract boolean isFinished();
	
	protected void buildSWTBase() {
		this.buildSWTBase(new String[0]);
	}
	
	protected void buildSWTBase(String[] args) {
		display = new Display();
		
		shell = new Shell(display);
		
		shell.setText(this.getWindowTitle());
		
		createGUI();
		
		createGUIControl();
		
		Iterable<Thread> threads = this.setUpApplet(args);
		
		shell.pack();
		shell.setSize(1000, 500);
		shell.open();
		while (!shell.isDisposed() && !this.isFinished()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		if(!shell.isDisposed()) {
			InfoDialog dialog = new InfoDialog(shell, SWT.NONE, "Closing threads...");
			dialog.setText("Wait until other threads finish");
			dialog.open();
			
			for(Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					// We have tried to wait. It was not possible: show the exception
					e.printStackTrace();
				}
			}
		}
		
		destroyGUI();
		display.dispose();
		
		System.exit(0);
	}

	protected void run(String[] args) {
		this.buildSWTBase(args);
	}
}
