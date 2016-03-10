package core;

import java.util.Random;
import java.util.TreeMap;
import java.util.LinkedList;
import java.io.*;

import core.Neuron;

public class Marvin
{
	public static Random random;
	private LinkedList<Neuron> neurons;
	private TreeMap<String, Neuron> input;
	private static final int TRIGGERVALUE = 10;

	public static void main(String args[])
	{
		System.out.println("\nMarvin\n");

		random = new Random(System.nanoTime());
		new Marvin();
	}

	public Marvin()
	{
		neurons = new LinkedList<Neuron>();
		input = new TreeMap<String, Neuron>();

		newOutputNeuron("a");
		newOutputNeuron("b");
		newOutputNeuron("c");
		newOutputNeuron("d");
		newOutputNeuron("e");
		newOutputNeuron("f");
		newOutputNeuron("g");
		newOutputNeuron("h");
		newOutputNeuron("i");
		newOutputNeuron("j");
		newOutputNeuron("k");
		run();
	}

	private Neuron newNeuron()
	{
		Neuron n = new Neuron(this);
		getNeurons().add(n);
		return n;
	}

	private Neuron newOutputNeuron(String output)
	{
		Neuron n = new OutputNeuron(output);
		getNeurons().add(n);
		return n;
	}

	private void run()
	{
		String command = null;
		do
		{
			command = getInput("command/exit");
			if (command.equals("exit"))
				break;
			handleCommand(command);
			while (isThinking())
			{
				tick();
			}
			giveFeedback(getInput("Feedback [y = good]").equals("y"));
		} while (true);
	}

	private void handleCommand(String command)
	{
		if (input.containsKey(command))
		{
			input.get(command).invoke(TRIGGERVALUE);
		}
		else
		{
			Neuron n = newNeuron();
			input.put(command, n);
			n.invoke(TRIGGERVALUE);
		}
		
	}

	private boolean isThinking()
	{
		return (getNumberOfThinkingNeurons() > 0);
	}

	private int getNumberOfThinkingNeurons()
	{
		int neuronsthinking = 0;
		for (Neuron neuron : getNeurons())
		{
			if (neuron.getValue() > 0)
			{
				neuronsthinking++;
			}
		}
		return neuronsthinking;
	}

	private void giveFeedback(boolean feedback)
	{
		if (feedback)
		{
			for (Neuron neuron : getNeurons())
			{
				neuron.good();
			}
		}
		else
		{
			for (Neuron neuron : getNeurons())
			{
				neuron.bad();
			}
		}
	}

	private void tick()
	{
		tickNeurons();
		System.out.println("\n" + getNumberOfThinkingNeurons() + " Neuronen sind aktiv");
	}

	private void tickNeurons()
	{
		for (Neuron neuron : getNeurons())
		{
			neuron.tick();
		}
	}

	public static String getInput(String text)
	{
		System.out.print(text + " >> ");
		try
		{
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			return input.readLine();
		} catch (Exception e) { }
		return null;
	}

	public LinkedList<Neuron> getNeurons() { return neurons; }
}
