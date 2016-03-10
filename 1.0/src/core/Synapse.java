package core;

import core.Neuron;

public class Synapse
{
	private Neuron target;
	private int value;
	private int strength = 0;

	public Synapse(Neuron target)
	{
		this.target = target;
		value = 0;
	}

	public void tick()
	{
		if (getValue() > 0)
		{
			getTarget().invoke(getValue());
			value = 0;
		}
	}

	public void bad()
	{
		strength--;
	}

	// Getter
	public int getValue() { return value; }
	public Neuron getTarget() { return target; }
	public void invoke(int value) { this.value = value; }
	public void good() { strength += 2; }
	public int getStrength() { return strength; }
	public boolean isStrong() { return strength > 0; }
}
