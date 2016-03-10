package brain;

import java.util.LinkedList;

import misc.Debug;

public class Marvin
{
	private LinkedList<InputNeuron> inputNeurons;
	private LinkedList<Neuron> activeNeurons;
	private LinkedList<Neuron> neurons;

	public Marvin()
	{
		inputNeurons = new LinkedList<InputNeuron>();
		activeNeurons = new LinkedList<Neuron>();
		neurons = new LinkedList<Neuron>();
		Neuron.init(this);
	}

	public void input(boolean[] triggers)
	{
		int diff = triggers.length - getInputNeurons().size();
		if (diff > 0) // es gibt mehr inputTriggers, als Input Neuronen
		{
			// Neue Input-Neuronen erstellen
			for (int i = 0; i < diff; i++)
			{
				getInputNeurons().add(new InputNeuron());
			}
		}

		for (int j = 0; j < triggers.length; j++)
		{
			if (triggers[j])
			{
				getInputNeurons().get(j).activate();
			}
		}
	}

	// returnt, ob ein Neuron existiert, dass genau die Trigger Neuronen hat, die grade aktiv sind
	private boolean hasTriggerNeuron()
	{
		for (Neuron neuron : getNeurons())
		{
			if (neuron.hasThisTriggers(getActiveNeurons()))
			{
				return true;
			}
		}
		return false;
	}

	public void think()
	{
		while (isThinking())
		{
			System.out.println("");
			if (!hasTriggerNeuron()) // Wenn kein Neuron existiert, dass die Trigger hat, die grade aktiv sind
			{
				Debug.warnIf(getActiveNeurons() == null, "Marvin.think(): getActiveNeurons == null");
				getNeurons().add(new Neuron(getActiveNeurons()));
				Debug.note("Neues Neuron mit " + getActiveNeurons().size() + " triggern", Debug.Tags.CREATE);
			}
			else { Debug.note("kein neues Neuron erstellt, da das Neuron schon existierte", Debug.Tags.PROCESS); }

			getActiveNeurons().clear();
			Debug.note("aktive Neuronen gecleart", Debug.Tags.PROCESS);
			for (int i = 0; i < getNeurons().size(); i++)
			{
				getNeurons().get(i).tick();
			}
			Debug.note(getNeurons().size() + " Neuronen wurden getickt", Debug.Tags.PROCESS);
			for (int i = 0; i < getInputNeurons().size(); i++)
			{
				getInputNeurons().get(i).tick();
			}
			Debug.note(getInputNeurons().size() + " Input-Neuronen wurden getickt", Debug.Tags.PROCESS);

			// Zahl der aktiven Input-Neuronen ermitteln:
			int inputneuronsnumber = 0;
			for (int i = 0; i < getInputNeurons().size(); i++)
			{
				if (getInputNeurons().get(i).isActive())
				{
					inputneuronsnumber++;
				}
			}
			Debug.note("Zahl der aktiven Input-Neuronen: " + inputneuronsnumber, Debug.Tags.PROCESS);
			Debug.note("Zahl der aktiven Neuronen = " + getActiveNeurons().size() + "\t - Zahl der Neuronen insgesamt = " + (getNeurons().size() + getInputNeurons().size()), Debug.Tags.PROCESS);
		}
	}

	private boolean isThinking() { return getActiveNeurons().size() != 0; }

	public void addActiveNeuron(Neuron neuron)
	{
		Debug.warnIf(!neuron.isActive(), "Marvin.addActiveNeuron(): Neuron is not active");
		getActiveNeurons().add(neuron);
	}

	private LinkedList<Neuron> getActiveNeurons()
	{
		Debug.warnIf(activeNeurons == null, "Marvin.getActiveNeurons(): activeNeurons == null");
		return activeNeurons;
	}

	private LinkedList<InputNeuron> getInputNeurons()
	{
		Debug.warnIf(inputNeurons == null, "Marvin.getInputNeurons(): inputNeurons == null");
		return inputNeurons;
	}

	private LinkedList<Neuron> getNeurons()
	{
		Debug.warnIf(neurons == null, "Marvin.getNeurons(): neurons == null");
		return neurons;
	}
}
