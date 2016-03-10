package core;

import marvin.Marvin;
import misc.Debug;

public class Main
{
	private Marvin marvin;

	public static void main(String[] args)
	{
		new Main();
	}

	public Main()
	{
		marvin = new Marvin();

		Debug.note("Marvin end");
	}
}
