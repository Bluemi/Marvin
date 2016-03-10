package brain.neurons;

import brain.neurons.Neuron;

public class InputNeuron extends Neuron
{
	char myTrigger;
	public InputNeuron(char myTrigger)
	{
		this.myTrigger = myTrigger;
	}
}
