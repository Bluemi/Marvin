package misc;

import java.io.*;
import java.util.LinkedList;
import java.text.DecimalFormat;

public class Debug
{
	public static final String STANDARD_CONFIGFILE = ".debug";
	public static final String STANDARD_LOGFILE = "log";
	public static final int STANDART_WARN_LEN = 5;
	public static final int STANDART_ERR_LEN = 20;

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

	private static boolean WRITE_TO_SCREEN = false, WRITE_TO_LOGFILE = false;
	private static int ERR_LEN = STANDART_ERR_LEN, WARN_LEN = STANDART_WARN_LEN;

	public static enum Tags
	{
		TAG(false),
		PROCESS(true),
		CREATE(true);

		private boolean active;

		Tags(boolean active)
		{
			this.active = active;
		}

		public boolean isActive() { return active; }
	};

	private static long lastTime = 0;

	private Debug() {}

	// public
	public static void init()
	{
		loadConfigFile();
		if (WRITE_TO_LOGFILE)
		{
			try
			{
				PrintWriter pw = new PrintWriter(STANDARD_LOGFILE);
				pw.close();
			} catch (Exception e)
			{
				error("failed to init Debug");
			}
		}
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

	// test
	public static void testTrace(String string)
	{
		colorLog(TEST_COLOR, "TEST: " + string);
		StackTraceElement[] trace = new Throwable().getStackTrace();
		for (int i = 1; i < Math.min(trace.length, 20); i++)
		{
			colorLog(TEST_COLOR, "\t" + trace[i]);
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
		if (b && tag.isActive())
		{
			warn(s, new Throwable().getStackTrace());
		}
	}

	public static void warnIf(boolean b, String s)
	{
		if (b)
		{
			warn(s, new Throwable().getStackTrace());
		}
	}

	public static void warn(String string, Tags tag)
	{
		if (tag.isActive())
		{
			warn(string, new Throwable().getStackTrace());
		}
	}

	public static void warn(String string)
	{
		warn(string, new Throwable().getStackTrace());
	}

	private static void warn(String string, StackTraceElement[] trace)
	{
		colorLog(WARN_COLOR, "WARN: " + string);
		for (int i = 1; i < Math.min(trace.length, WARN_LEN); i++)
		{
			colorLog(ERROR_COLOR, "\t" + trace[i]);
		}
	}

	// error
	public static void error(String string, Tags tag)
	{
		if (tag.isActive())
		{
			error(string, new Throwable().getStackTrace());
		}
	}

	public static void error(String string) // quits (with exception) and gives error message
	{
		error(string, new Throwable().getStackTrace());
	}

	private static void error(String string, StackTraceElement[] trace)
	{
		colorLog(ERROR_COLOR, "ERROR: " + string, true);
		for (int i = 1; i < Math.min(trace.length, ERR_LEN); i++)
		{
			colorLog(ERROR_COLOR, "\t" + trace[i], true);
		}
		System.exit(1);
	}

	// color
	private static void colorLog(String color, String string)
	{
		colorLog(color, string, false);
	}

	private static void colorLog(String color, String string, boolean force)
	{
		if (WRITE_TO_SCREEN || force)
		{
			System.out.print(color);
		}
		log(string, force);
		if (WRITE_TO_SCREEN || force)
		{
			System.out.print(RESET); // resets color
		}
	}

	// kernel-deep

	private static void log(String string)
	{
		log(string, false);
	}

	private static void log(String string, boolean force)
	{
		if (WRITE_TO_SCREEN || force)
		{
			System.out.println(string); // prints log
		}
		if (WRITE_TO_LOGFILE || force)
		{
			writeLog(string);
		}
	}

	private static void loadConfigFile()
	{
		if (new File(STANDARD_CONFIGFILE).isFile())
		{
			BufferedReader br = null;
			try
			{
				br = new BufferedReader(new FileReader(STANDARD_CONFIGFILE));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null)
				{
					if (line.equals("logfile=on"))
						WRITE_TO_LOGFILE = true;
					if (line.equals("screen=on"))
						WRITE_TO_SCREEN = true;
					if (line.startsWith("warnlen="))
						WARN_LEN = Integer.parseInt(line.substring(8));
					if (line.startsWith("errlen="))
						ERR_LEN = Integer.parseInt(line.substring(7));
				}
			} catch (Exception e)
			{
				System.out.println("Debug.loadConfigFile(): error loading config-file \"" + STANDARD_CONFIGFILE + "\"");
			}
			finally
			{
				try
				{
					br.close();
				} catch (Exception e)
				{
					System.out.println("Debug.loadConfigFile(): error while closing br");
				}
			}
		}
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
		} catch (Exception e)
		{
			error("failed to write Log to file " + STANDARD_LOGFILE);
		}
	}
}
