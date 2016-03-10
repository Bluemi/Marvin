package main;

import java.util.Scanner;

import brain.Marvin;
import misc.Debug;

public class Main
{
	public static final char triggerChar = 'I';
	public static final char ignoreChar = 'O';
	public static final String quitString = "q";

	public static Marvin marvin;

	public static void main(String[] args)
	{
		System.out.println("\n---   Marvin   ---\n");
		Scanner scan = new Scanner(System.in);
		Debug.init();

		marvin = new Marvin();

		String input;
		boolean[] triggers;

		do
		{
			System.out.print("» ");
			input = scan.next();
			if (isValidString(input))
			{
				triggers = StringToBooleanArray(input);
				marvin.input(triggers);
				marvin.think();
			}
			else if (input.equals(quitString))
			{
				System.out.println("\n---   Marvin   ---\n");
			}
			else
			{
				System.out.println("Invalid input. Do a combination of \'" + triggerChar + "\' and \'" + ignoreChar + "\' or \"" + quitString + "\" to quit");
			}
		}
		while (!input.equals(quitString));
	}

	public static boolean isValidString(String s)
	{
		for (int i = 0; i < s.length(); i++)
		{
			if (!((s.charAt(i) == ignoreChar) || (s.charAt(i) == triggerChar)))
			{
				return false;
			}
		}
		return true;
	}

	public static boolean[] StringToBooleanArray(String s)
	{
		boolean[] bools = new boolean[s.length()];

		for (int i = 0; i < s.length(); i++)
		{
			if (s.charAt(i) == triggerChar)
			{
				bools[i] = true;
			}
			else if (s.charAt(i) == ignoreChar)
			{
				bools[i] = false;
			}
			else
			{
				System.out.println("Main.StringToBooleanArray(): invalid Char at " + i);
				return null;
			}
		}
		return bools;
	}
}
