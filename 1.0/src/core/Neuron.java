package core;

import java.util.Random;
import java.util.LinkedList;

import core.Synapse;
import core.Marvin;

public class Neuron
{
	private LinkedList<Synapse> synapses;
	private int value;
	private static final int DECREASEMENT = 10;
	private Marvin marvin;

	public Neuron(Marvin marvin)
	{
		synapses = new LinkedList<Synapse>();		
		this.marvin = marvin;

		value = 0;
	}

	public void invoke(int value)
	{
		setValue(getValue() + value);
		invokeSynapses();
	}

	public void tick()
	{
		tickSynapses();
		value = Math.max(0, getValue() - DECREASEMENT);
	}

	private void tickSynapses()
	{
		for (Synapse synapse : getSynapses())
		{
			synapse.tick();
		}		
	}

	private void invokeSynapses()
	{
		if (getSynapses().size() == 0)
		{
			synapses.add(new Synapse(marvin.getNeurons().get(Marvin.random.nextInt(marvin.getNeurons().size()))));
		}
		for (Synapse synapse : getSynapses())
		{
			synapse.invoke(value);
		}
	}

	public void bad()
	{
		int i = 0;
		while (i < getSynapses().size())
		{
			getSynapses().get(i).bad();
			if (!getSynapses().get(i).isStrong())
			{
				getSynapses().remove(i);
			}
			else
			{
				i++;
			}
		}
	}

	public void good()
	{
		for (Synapse synapse : getSynapses())
		{
			synapse.good();
		}
	}

	public int getValue() { return value; }
	public LinkedList<Synapse> getSynapses() { return synapses; }

	private void setValue(int value) { this.value = value; }
}
