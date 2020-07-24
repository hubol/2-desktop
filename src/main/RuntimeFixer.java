package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;

import javax.swing.JOptionPane;
/**
 * This class can ensure that the JVM has certain desired properties before starting the program as a jar:<br>
 * -The memory limit must be above or equal to DEMANDED_MEMORY<br>
 * -The working directory must be equal to the directory where the jar lies<br>
 * <br>
 * This is problematic because these things cannot be changed after the creation of the JVM, so a new one must be created. 
 * The new JVM is launched in a way that has been tested on Linux and Windows. 
 * In addition, the output from any new process is redirected to this process' command line output.  
 * */
public class RuntimeFixer
	{
	/**Minimum demanded memory limit, in Megabytes. Default 256M*/
	public static int DEMANDED_MEMORY = 256;
	
	/**This method should be run from the main method.<br/>
	 * If it returns true, the program should start normally.<br/>
	 * If it returns false, the program must terminate immediately.<br/>
	 * 
	 * Usage example:<br/>
	 * <pre>
	 * public static void main(String[] args){
	 * 	RuntimeFixer.DEMANDED_MEMORY = 256;
	 *	if(RuntimeFixer.validateProcess()){
	 *		//Program code
	 *	}
	 * }
	 * </pre>
	 * The class' start() method will be called when it has been made sure that:<br/>
	 * -The current JVM's memory limit is larger than or equal to DEMANDED_MEMORY<br/>  
	 * -The current JVM's working directory is equal to the directory where the jar file lies<br/>
	 *<br/> 
	 * The method will fail and exit the program if the conditions are not met and we are not running from a jar file.<br/>
	 * The class will attempt to create a new JVM process with the desired properties if these conditions are not met.<br/>
	 *<br/> 
	 * If a new process is created:<br/> 
	 * -This method will block until the new process is destroyed, e.g. the output stream terminates.<br/> 
	 * -The command line output from the new process will be redirected to the old process.<br/>
	 * -The new process will survive even if the old process is destroyed.
	 * */
	public static boolean validateProcess()
		{
		//Find memory limit:
		long memory = Runtime.getRuntime().maxMemory();
		System.out.println("Memory limit: "+memory);
		//Find out if this is a running jar:
		boolean runningFromJar = Main.class.getResource("Main.class").toString().startsWith("jar");
		
		//Get the full path to the _current_ JVM (Trusted sources claim that this indeed works):
		String javaPath = System.getProperty("java.home")+"\\bin\\java";
		javaPath = javaPath.replace('\\', '/');//Forward slash supported by more systems
		
		
		//Find the directory we're _actually_ running from(double clicky jar on linux yields a nonsense working directory):
		String jarDirectory = Main.DIRECTORY;
		
		//Find the current working directory(Note: May be unusable)
		String workingDir = new File(".").getAbsolutePath();
		workingDir = workingDir.substring(0, workingDir.length()-1);
		workingDir = workingDir.replace('\\', '/');//Forward slash supported by more systems
		
		//Find out if the working directory is incorrect(e.g. double clicking a jar on linux)
		boolean workingDirIsCorrupt = (runningFromJar && !workingDir.equals(jarDirectory) && 
				!workingDir.equals(jarDirectory.substring(1)));//Windows includes a redundant "/" at start...
		
		if(memory >= DEMANDED_MEMORY*1000000 && !workingDirIsCorrupt)
			{
			System.out.println("Sufficient memory, starting the process.");
			return true;
			}
		else
			{
			try
				{
				if(!runningFromJar)
					{
					JOptionPane.showMessageDialog(null,"There is not enough memory.\n" +
							"A new process with more memory cannot be started\n"+
							"because the program was not run as a jar.","Not enough memory",
							JOptionPane.ERROR_MESSAGE);
					System.exit(1);
					}
				
				if(workingDirIsCorrupt)
					System.out.println("Working directory is malformed, creating a new process.");
				else
					System.out.println("Insufficient memory, creating a new process.");
				ProcessBuilder p = new ProcessBuilder(javaPath, "-jar", 
						"-Xms"+DEMANDED_MEMORY+"m", "-Xmx"+DEMANDED_MEMORY+"m", getJarName());
				p.redirectErrorStream(true);//Merge err and out for the new process, for easy redirecting.
				p.directory(new File(jarDirectory));
				Process process = p.start();
				System.out.println("Success! Redirecting the new process' output:");
				//The following call blocks until the new process' output ends:
				redirect(process.getInputStream());
				}
			catch (Exception e)
				{
				JOptionPane.showMessageDialog(null,
						"Could not launch a new process with sufficient memory.\n" +
						"\n" +
						"Attempted to use JVM: "+javaPath+"\n" +
						"Attempted to use Jar: "+jarDirectory+getJarName()+"\n" +
						e.toString(), "Process creation failed",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				}
			}
		return false;
		}
	
	/**Get the file name of the jar. Returns null if not running from a jar.*/
	public static String getJarName()
		{
		String str = Main.class.getResource("Main.class").toString();
		int startPos = str.lastIndexOf(".jar");
		String jarName = null;
		if (startPos!=-1)
			{
			int endPos = startPos+4;
			while(startPos>0)
				{
				startPos--;
				if(str.charAt(startPos)=='/' || str.charAt(startPos)=='\\')
					{
					startPos++;
					break;
					}
				}
			jarName = str.substring(startPos,endPos);
			}
		return jarName;
		}
	
	private static void redirect(InputStream inputStream)
		{
		BufferedReader buffer;
		buffer = new BufferedReader(new InputStreamReader(inputStream));
		
		String in;
		try
			{
			while((in=buffer.readLine())!=null)
				{
				System.out.println(in);
				}
			System.out.println("The stream being redirected terminated normally.");
			}
		catch (IOException e)
			{
			System.err.println("The stream being redirected seems to have died unexpectedly.");
			e.printStackTrace();
			}
		}
	}
