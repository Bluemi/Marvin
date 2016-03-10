package core;

public class OutputNeuron extends Neuron
{
	private String output;

	public OutputNeuron(String output)
	{
		super(null);
		this.output = output;
	}

	@Override public void invoke(int value)
	{
		System.out.print(output);
	}
}
