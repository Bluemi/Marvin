package misc;

import java.io.*;
import java.util.LinkedList;
import java.text.DecimalFormat;

public class Debug
{
	public static final String RESET = "\u001B[0m";

	public static final String FBLACK = "\u001B[30m";
	public static final String FRED = "\u001B[31m";
	public static final String FGREEN = "\u001B[32m";
	public static final String FYELLOW = "\u001B[33m";
	public static final String FBLUE = "\u001B[34m";
	public static final String FPURPLE = "\u001B[35m";
	public static final String FCYAN = "\u001B[36m";
	public static final String FWHITE = "\u001B[37m";

	public static final String BBLACK = "\u001B[40m";
	public static final String BRED = "\u001B[41m";
	public static final String BGREEN = "\u001B[42m";
	public static final String BYELLOW = "\u001B[43m";
	public static final String BBLUE = "\u001B[44m";
	public static final String BPURPLE = "\u001B[45m";
	public static final String BCYAN = "\u001B[46m";
	public static final String BWHITE = "\u001B[47m";

	public static final String NOTE_COLOR = FYELLOW;
	public static final String ONCE_COLOR = FYELLOW;
	public static final String TIME_COLOR = FCYAN;
	public static final String TEST_COLOR = FPURPLE;
	public static final String WARN_COLOR = FRED;
	public static final String ERROR_COLOR = FRED;

	public static enum Tags
	{
		TRIGG(true),
		CREATE(true);

		private boolean active;

		Tags(boolean active)
		{
			this.active = active;
		}

		public boolean isActive() { return active; }
	};

	private static LinkedList<String> logs = new LinkedList<String>(); // liste der logs
	private static final String STANDARD_LOGFILE = "log"; // standart-logfile
	private static long lastTime = 0;

	private Debug() {}

	// public
	public static void init()
	{
		try
		{
			PrintWriter pw = new PrintWriter(STANDARD_LOGFILE);
			pw.close();	
		} catch (Exception e) { error("failed to init Debug"); }
	}

	// note
	public static void note(String string, Tags tag)
	{
		if (tag.isActive())
		{
			note(string);
		}
	}
	public static void note(String string)
	{
		colorLog(NOTE_COLOR, "NOTE: " + string);
	}

	// once
	public static void once(String string, Tags tag)
	{
		if (tag.isActive())
		{
			once(string);
		}
	}
	public static void once(String string)
	{
		if (!isLogged("ONCE: " + string))
		{
			colorLog(ONCE_COLOR, "ONCE: " + string);
		}
	}

	// test
	public static void test(String string, Tags tag)
	{
		if (tag.isActive())
		{
			test(string);
		}
	}
	public static void test(String string)
	{
		colorLog(TEST_COLOR, "TEST: " + string);
	}
	// time
	public static void time(String string, Tags tag)
	{
		if (tag.isActive())
		{
			time(string);
		}
	}
	public static void time(String string) // prints time difference of last timeLog(...)
	{
		if (lastTime == 0) // if there was no last timeLog()
		{
			colorLog(TIME_COLOR, "TIME: " + string + ": first call");
		}
		else // if there was
		{
			DecimalFormat df = new DecimalFormat("#.###"); // create DecimalFormat
			string += ": " + df.format((System.nanoTime() - lastTime)/1000000000D) + " seconds";
			colorLog(TIME_COLOR, "TIME: " + string);
		}
		lastTime = System.nanoTime(); // set lastTime to now
	}

	// warn
	public static void warnIf(boolean b, String s, Tags tag)
	{
		if (b)
			warn(s, tag);
	}

	public static void warnIf(boolean b, String s)
	{
		if (b)
			warn(s);
	}

	public static void warn(String string, Tags tag)
	{
		if (tag.isActive())
		{
			warn(string);
		}
	}
	public static void warn(String string)
	{
		colorLog(WARN_COLOR, "WARN: " + string);
		colorLog(WARN_COLOR, "\t" + new Throwable().getStackTrace()[2]);
	}

	// error
	public static void error(String string, Tags tag)
	{
		if (tag.isActive())
		{
			error(string);
		}
	}
	public static void error(String string) // quits (with exception) and gives error message
	{
		colorLog(ERROR_COLOR, "ERROR: " + string);
		StackTraceElement[] trace = new Throwable().getStackTrace();
		for (int i = 1; i < trace.length; i++)
		{
			colorLog(ERROR_COLOR, "\t" + trace[i]);
		}
		System.exit(1);
	}

	public static boolean isLogged(String string) // check if the string was a log
	{
		return logs.contains(string);
	}

	private static void log(String string) // prints log and adds it to logs
	{
		System.out.println(string); // prints log
		addLog(string); // adds it to logs
		writeLog(string);
	}

	private static void colorLog(String color, String string)
	{
		System.out.print(color);
		log(string);
		System.out.print(RESET); // resets color
	}

	private static void addLog(String string) // adds log to loglist
	{
		logs.add(string);
	}

	private static void writeLog(String string)
	{
		try
		{
			File dir = new File(".");
			String loc = dir.getCanonicalPath() + File.separator + STANDARD_LOGFILE;
	 
			FileWriter fstream = new FileWriter(loc, true);
			BufferedWriter out = new BufferedWriter(fstream);
	 
			out.write(string);
			out.newLine();
	 
			out.close();
		} catch (Exception e) { error("failed to write Log to file " + STANDARD_LOGFILE); }
	}
}
