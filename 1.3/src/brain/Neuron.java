/*
	Alle Neuronen bekommen im Konstruktor eine Liste von Neuronen übergeben, die alle active sein müssen, damit das Neuron selbst active wird.
*/

package brain;

import java.util.LinkedList;

import misc.Debug;

public class Neuron
{
	protected static Marvin marvin;
	private LinkedList<Neuron> triggerNeurons;
	private boolean active;

	public Neuron(LinkedList<Neuron> argNeurons)
	{
		if (!isInputNeuron())
		{
			triggerNeurons = new LinkedList<Neuron>();
			Debug.warnIf(argNeurons == null, "Neuron.<init>(): argNeurons == null");
			for (Neuron neuron : argNeurons)
			{
				Debug.warnIf(neuron == null, "Neuron.<init>(): neuron == null");
				triggerNeurons.add(neuron);
			}
		}
	}

	public static void init(Marvin marv)
	{
		Debug.warnIf(marv == null, "Neuron.init(): marv == null");
		marvin = marv;
	}

	protected LinkedList<Neuron> getTriggerNeurons()
	{
		Debug.warnIf(triggerNeurons == null, "Neuron.getTriggerNeurons(): triggerNeurons == null");
		return triggerNeurons;
	}

	public void tick()
	{
		if (allTriggersActive())
		{
			activate();
			System.out.println("Neuron tick true");
		}
		else
		{
			setActive(false);
			System.out.println("Neuron tick false");
		}
	}

	public boolean allTriggersActive()
	{
		for (Neuron neuron : getTriggerNeurons())
		{
			if (!neuron.isActive())
			{
				return false;
			}
		}
		return true;
	}

	public boolean hasThisTriggers(LinkedList<Neuron> neurons)
	{
		if (neurons.size() != getTriggerNeurons().size())
		{
			return false;
		}

		for (Neuron neuron : neurons)
		{
			if (!getTriggerNeurons().contains(neuron))
			{
				return false;
			}
		}
		return true; // Wenn die beiden Listen die gleiche Länge haben und jedes Element des einen auch im anderen vorkommt, so sind die beiden Listen identisch
	}

	public boolean isActive() { return active; }
	public void activate()
	{
		active = true;
		marvin.addActiveNeuron(this);
	}

	public void setActive(boolean b) { active = b; }
	protected boolean isInputNeuron() { return false; }
}
