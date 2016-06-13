package rioko.swt.applet.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerController {
	
	protected Integer inPort = null;
	protected Integer outPort = null;

	protected ServerSocket inServer = null;
	protected ServerSocket outServer = null;
	
	protected Socket inSocket = null;
	protected Socket outSocket = null;
	
	protected InputStream input = null;
	protected BufferedInputStream inBuff= null;
	protected OutputStream output = null;
	protected BufferedOutputStream outBuff = null;
	
	private boolean ended = false;
	
	public void run() {
		open();
		
		try {
			System.out.println(" - Opening servers...");
			inServer = new ServerSocket(inPort);
			inServer.setSoTimeout(ServerConstants.TIMEOUT_SERVERS);
			outServer = new ServerSocket(outPort);
			outServer.setSoTimeout(ServerConstants.TIMEOUT_SERVERS);
			System.out.println("Servers opened!");
		} catch (IOException e) {
			String message = "Error in the parameters of the program. We need 2 numbers which must be non-used ports";
			System.out.println(message);
			close();
			throw new RuntimeException(message, e);
		}
		
		while(!ended) {
			try {
				ServerConstants.INPUT_CONSOLE_STREAM.println("------------------------------------");
				ServerConstants.OUTPUT_CONSOLE_STREAM.println("------------------------------------");
				while(inSocket == null && !ended) {
					try {
						ServerConstants.INPUT_CONSOLE_STREAM.println(" - Waiting the input client...");
						inSocket = inServer.accept();
						ServerConstants.INPUT_CONSOLE_STREAM.println("Input client received!");
						ServerConstants.INPUT_CONSOLE_STREAM.println("Accepted input client: " + inSocket.getInetAddress().getHostAddress() + ":" + inSocket.getPort());
					} catch(SocketTimeoutException e) {
						//Do nothing
					}
				}
				while(outSocket == null && !ended) {
					try {
						ServerConstants.OUTPUT_CONSOLE_STREAM.println(" - Waiting the output client...");
						outSocket = outServer.accept();
						ServerConstants.OUTPUT_CONSOLE_STREAM.println("Output client received!");
						ServerConstants.OUTPUT_CONSOLE_STREAM.println("Accepted output client: " + outSocket.getInetAddress().getHostAddress() + ":" + outSocket.getPort());
					} catch(SocketTimeoutException e) {
						//Do nothing
					}
				}
			} catch (IOException e) {
				String message = "Error accepting the input and output clients";
				System.out.println(message);
				close();
				throw new RuntimeException(message, e);
			}
			
			if(!ended) {
				try {
					input = inSocket.getInputStream();
					inBuff = new BufferedInputStream(input, ServerConstants.BUFFER_SIZE);
					output = outSocket.getOutputStream();
					outBuff = new BufferedOutputStream(output, ServerConstants.BUFFER_SIZE);
				} catch (IOException e) {
					String message = "Error getting the Input and Output Streams for the clients";
					System.out.println(message);
					close();
					throw new RuntimeException(message, e);
				}
				
				byte[] buffer = new byte[ServerConstants.BUFFER_SIZE];
				while(!ended) {
					int read;
					try {
						read = inBuff.read(buffer, 0, ServerConstants.BUFFER_SIZE);
						ServerConstants.INPUT_CONSOLE_STREAM.println(" - Read " + read + " bytes from input");
						outBuff.write(buffer, 0, read);
						ServerConstants.OUTPUT_CONSOLE_STREAM.println(" - Sent " + read + " bytes to output");
					} catch (IOException e) {
						break;
					}
				}
			}
		}
		
		close();
	}
	
	public void dispose() {
		this.setEnded(true);
	}
	
	public void setInPort(Integer inPort) {
		if(inPort == null) throw new RuntimeException("Input Port must be non null");
		this.inPort = inPort;
	}
	
	public void setOutPort(Integer outPort) {
		if(outPort == null) throw new RuntimeException("Output port must be non null");
		this.outPort = outPort;
	}
	
	public void setEnded(boolean ended) {
		this.ended = ended;
	}
	
	public boolean getEnded() {
		return this.ended;
	}
	
	private void open() {
		System.out.println("---------------------------------------------------------------");
		System.out.println("###############################################################");
		System.out.println("---------------------------------------------------------------");
		
		System.out.println("*** Auxiliar server for two Socket Clients who want send data");
		System.out.println("***");
	}

	private void close() {
		System.out.println("***");
		System.out.println("*** Finished the execution of the Server");
		System.out.println("---------------------------------------------------------------");
		System.out.println("###############################################################");
		System.out.println("---------------------------------------------------------------");
	}
}
