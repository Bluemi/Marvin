package brain.synapses;

import misc.Debug;

public class Synapse
{
	private int id;

	public static int publicID = 0;

	public Synapse()
	{
		Debug.note("New Synapse created", Debug.Tags.CREATE);
		id = publicID;
		publicID++;
	}
}
