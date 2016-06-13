package rioko.swt.applet.basic.text;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public final class PrintableText extends Composite {
	
	private static HashMap<Integer, Text> mapToTexts = new HashMap<>();
	private static Integer numOfTexts = 0;
	
	private Integer textId = null;
	private String text = "";
	
	private PrintStream printStream;
	private ByteArrayOutputStream outputStream;
	
	public PrintableText(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FillLayout());

		this.textId = numOfTexts;
		numOfTexts++;
		mapToTexts.put(textId, new Text(this, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL));
		mapToTexts.get(textId).setEditable(false);
		mapToTexts.get(textId).setText(this.text);
		//Stream control
		outputStream = new ByteArrayOutputStream();
		printStream = this.createPrintStream(outputStream);
	}	

	public PrintStream getPrintStream() {
		return this.printStream;
	}
	
	private PrintStream createPrintStream(ByteArrayOutputStream outputStream2) {
		return new PrintStream(outputStream) {
			@Override
			public void print(boolean b) {
				text += b;
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						mapToTexts.get(textId).setText(text);
					}
				});
			}
			@Override
			public void print(char c) {
				text += c;
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						mapToTexts.get(textId).setText(text);
						mapToTexts.get(textId).setTopIndex(mapToTexts.get(textId).getLineCount()-1);
					}
				});
			}
			@Override
			public void print(char[] s) {
				text += (new String(s));
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						mapToTexts.get(textId).setText(text);
						mapToTexts.get(textId).setTopIndex(mapToTexts.get(textId).getLineCount()-1);
					}
				});
			}
			@Override
			public void print(double d) {
				text += d;
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						mapToTexts.get(textId).setText(text);
						mapToTexts.get(textId).setTopIndex(mapToTexts.get(textId).getLineCount()-1);
					}
				});
			}
			@Override
			public void print(int i) {
				text += i;
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						mapToTexts.get(textId).setText(text);
						mapToTexts.get(textId).setTopIndex(mapToTexts.get(textId).getLineCount()-1);
					}
				});
			}
			@Override
			public void print(long l) {
				text += l;
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						mapToTexts.get(textId).setText(text);
						mapToTexts.get(textId).setTopIndex(mapToTexts.get(textId).getLineCount()-1);
					}
				});
			}
			@Override
			public void print(float f) {
				text += f;
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						mapToTexts.get(textId).setText(text);
						mapToTexts.get(textId).setTopIndex(mapToTexts.get(textId).getLineCount()-1);
					}
				});
			}
			@Override
			public void print(String s) {
				text += s;
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						mapToTexts.get(textId).setText(text);
						mapToTexts.get(textId).setTopIndex(mapToTexts.get(textId).getLineCount()-1);
					}
				});
			}
			@Override
			public void print(Object obj) {
				text += obj.toString();
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						mapToTexts.get(textId).setText(text);
						mapToTexts.get(textId).setTopIndex(mapToTexts.get(textId).getLineCount()-1);
					}
				});
			}
			@Override
			public void println() {
				print('\n');
			}
			@Override
			public void println(boolean x) {
				print(x); println();
			}
			@Override
			public void println(char x) {
				print(x); println();
			}
			@Override
			public void println(int x) {
				print(x); println();
			}
			@Override
			public void println(long x) {
				print(x); println();
			}
			@Override
			public void println(float x) {
				print(x); println();
			}
			@Override
			public void println(double x) {
				print(x); println();
			}
			@Override
			public void println(char[] x) {
				print(x); println();
			}
			@Override
			public void println(String x) {
				print(x); println();
			}
			@Override
			public void println(Object x) {
				print(x); println();
			}			
		};
	}
}
