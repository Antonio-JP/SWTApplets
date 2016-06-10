package rioko.swt.applet.server;

import java.io.PrintStream;

public class ServerConstants {
	public static final int BUFFER_SIZE = 10*1024;
	
	public static final int DEFAULT_INPUT_PORT = 9990;
	public static final int DEFAULT_OUTPUT_PORT = 9991;
	
	public static final int TIMEOUT_SERVERS = 1000*5;
	
	public static PrintStream INPUT_CONSOLE_STREAM = System.out;
	public static PrintStream OUTPUT_CONSOLE_STREAM = System.out;
}
