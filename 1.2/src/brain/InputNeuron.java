package brain;

public class InputNeuron extends Neuron
{
	public InputNeuron()
	{
		super(null); // InputNeurons have no TriggerNeurons
	}

	@Override public void tick()
	{
		if (isActive())
		{
			setActive(false);
		}
	}

	@Override protected boolean isInputNeuron() { return true; }
}
