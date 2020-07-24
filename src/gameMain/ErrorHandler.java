package gameMain;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ErrorHandler
	{
	PrintStream errStream;
	DetectorStream detectStream;
	ByteArrayOutputStream errOutput;
	
	private String errors;
	JTextArea textArea;
	JFrame frame;
	
	public ErrorHandler()
		{
		//Nop
		}
	
	/**Create a PrintStream that will display a frame with whatever is written to it, after any newlines.*/
	public PrintStream getStream()
		{
		if(errStream!=null)
			throw new RuntimeException("Duplicate Error Handler");
		
		errOutput = new ByteArrayOutputStream();
		detectStream = new DetectorStream(errOutput);
		errStream = new PrintStream(detectStream);
		
		return errStream;
		}

	/**Called when something has been written to System.err*/
	protected void detect()
		{
		final String message = errOutput.toString();
		final String separator = System.getProperty("line.separator");
		
		//Test if there is a newline written to the stream
		if(message.contains(separator))
			{
			//Reset the stream, ready for next line
			errOutput.reset();
			
			SwingUtilities.invokeLater(new Runnable()
				{
				@Override public void run()
					{
					//System.out.println("Line:"+message+":END");
					if(message.contains("INFO: ") || message.contains("WARNING: "))
						{
						System.out.println("Ignoring error output from JPen: ");
						System.out.println(message);
						} 
					else if(errors==null)
						//Append the line to the output window, displaying it if it is not visible
						{
						errors = message;
						textArea = new JTextArea(24,32);
						textArea.setText(errors);
						textArea.setLineWrap(false);
						textArea.setForeground(Color.RED);
						JScrollPane scrollPane = new JScrollPane(textArea);
						
						frame = new JFrame("Crash Log");
						frame.add(scrollPane);
						frame.pack();
						frame.setVisible(true);
						}
					else
						{
						errors+=message;
						textArea.setText(errors);
						frame.setVisible(true);
						}
					}
				});
			}
		}

	/**This class simply writes all data to a destination stream, then notifies the parent ErrorHandler.*/
	private class DetectorStream extends OutputStream
		{
		OutputStream dest;
		public DetectorStream(OutputStream dest)
			{
			this.dest = dest;
			}

		@Override
		public void write(int b) throws IOException
			{
			dest.write(b);
			detect();
			}
		@Override
		public void write(byte[] b) throws IOException
			{
			dest.write(b);
			detect();
			}
		@Override
		public void write(byte[] b, int off, int len) throws IOException
			{
			dest.write(b, off, len);
			detect();
			}
		}
	}
