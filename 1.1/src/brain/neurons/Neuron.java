package brain.neurons;

import brain.Triggable;
import brain.synapses.Synapse;
import misc.Debug;

public class Neuron implements Triggable
{
	LinkedList<Synapse> synapses;

	public Neuron()
	{
		synapses = new LinkedList<Synapse>();
	}

	@Override public void trigger(int index, float force)
	{
		Debug.note("Neuron triggered", Debug.Tags.TRIGG);
	}
}
