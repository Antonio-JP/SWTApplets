package rioko.swt.applet.console_client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

public class ConsoleController 
{
	private static ConsoleController INSTANCE;
	
	private ConsoleApplet applet = null;
	
	private Integer port = null;
	
	private Socket socket = null;
	private OutputStream output = null;
	private InputStream input = null;
	
	private ArrayList<ClosableThread> threads = new ArrayList<>();
	
	private ConsoleController(ConsoleApplet applet) {this.applet = applet;}
	
	public static ConsoleController getController(ConsoleApplet applet) {
		if(INSTANCE == null) { INSTANCE = new ConsoleController(applet); }
		INSTANCE.applet = applet;
		return INSTANCE;
	}
	
	public boolean connect(String host, String port) {
		if(this.isConnected()) {
			return false;
		}
		
		try {
			this.port = Integer.parseInt(port);
		} catch(NumberFormatException e) {
			this.close();
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("*** ERROR CONNECTING TO SERVER\n"
					+ "\t- The port must be an Integer.");
			return false;
		}
		
		try {
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("************************************************************");
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("--- Trying to connect to server on " + host + ":" + this.port);
			this.socket = new Socket(host, this.port);
			this.socket.setSoTimeout(1000*3);
		} catch (IOException e) {
			this.close();
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("*** ERROR CONNECTING TO SERVER\n"
					+ "\t- The server can not be reached.");
			return false;
		}
		
		try {
			output = socket.getOutputStream();
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("--- Got an output stream from the socket");
		} catch (IOException e) {
			this.close();
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("*** ERROR CONNECTING TO SERVER\n"
					+ "\t- The server can not be reached.");
			return false;
		}
		try {
			input = socket.getInputStream();
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("--- Got an input stream from the socket");
		} catch (IOException e) {
			this.close();
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("***** ERROR CONNECTING TO SERVER\n"
					+ "\t- The server can not be reached.");
			return false;
		}
		
		//Opening two threads to read and send data
		ClosableThread inputThread = new ClosableThread() {
			@Override
			public void run() {
				ConsoleController controller = ConsoleController.INSTANCE;
				byte[] read = new byte[ConsoleConstants.BUFF_SIZE];
				
				while(!this.end) {
					try {
						try {
							controller.input.read(read);
							ConsoleConstants.OUTPUT_CONSOLE_STREAM.print("Remote: " + new String(read));
						} catch (SocketTimeoutException e) {
							//Do nothing, just rechecking conditions
						}
					} catch (IOException e) {
						//Error on the Stream -- disconnected
						break;
					}
				}
				(new Thread() {public void run(){ ConsoleController.INSTANCE.close(); }}).start();
			}
		};
		threads.add(inputThread);
		
		inputThread.start();
		ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("--- Input thread listening...");
		
		return true;
	}
	
	public boolean sendMessage(String message) {
		if(this.output != null) {
			byte[] toSend = new byte[message.length()];
			for(int i = 0; i < message.length(); i++) {
				toSend[i] = (byte)message.charAt(i);
			}
			try {
				this.output.write(toSend);
				ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("You: " + message);
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isConnected() {
		return this.port != null;
	}
	
	public void close() {
		if(!threads.isEmpty()) {
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("--- Closing threads...");
			for(ClosableThread thread : threads) {
				thread.end();
				try {
					thread.join();
				} catch (InterruptedException e) {
					// Show in the console
					e.printStackTrace();
				}
			}
			threads.clear();
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("--- Threads closed!*");
		}
		
		if(socket != null) {
			try {
				ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("--- Closing socket...");
				socket.close();
			} catch (IOException e) {
				// Show in the console
				e.printStackTrace();
			}
			socket = null;
			ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("--- Socket closed!*");
		}
		
		if(port != null) {
			port = null;
		}
		ConsoleConstants.OUTPUT_CONSOLE_STREAM.println("************************************************************");
		Display.getDefault().asyncExec(new Runnable() { public void run() {
			ConsoleController.INSTANCE.applet.changeToDisconnected(); 
		}});
		
	}

	private class ClosableThread extends Thread {
		protected boolean end = false;
		
		public void end() {
			this.end = true;
		}
	}
}
