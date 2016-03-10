package marvin;

import java.util.LinkedList;

import brain.neurons.Neuron;
import misc.Debug;

public class Marvin
{
	private LinkedList<Neuron> neurons;

	public Marvin()
	{
		Debug.note("New Marvin created", Debug.Tags.CREATE);

		neurons = new LinkedList<Neuron>();
	}

	public void think(String input)
	{
		
	}
}
