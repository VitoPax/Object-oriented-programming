package hydraulic;

/**
 * Represents a multisplit element, an extension of the Split that allows many outputs
 * 
 * During the simulation each downstream element will
 * receive a stream that is determined by the proportions.
 */

public class Multisplit extends Split {

	private double[] proportions;
	

	/**
	 * Constructor
	 * @param name the name of the multi-split element
	 * @param numOutput the number of outputs
	 */
	public Multisplit(String name, int numOutput) {
		super(name);
		this.outputs = new Element[numOutput];
	}
	
	/**
	 * Define the proportion of the output flows w.r.t. the input flow.
	 * 
	 * The sum of the proportions should be 1.0 and 
	 * the number of proportions should be equals to the number of outputs.
	 * Otherwise a check would detect an error.
	 * 
	 * @param proportions the proportions of flow for each output
	 */
	public void setProportions(double... proportions) {
		if (proportions.length != outputs.length) {
			throw new IllegalArgumentException("Number of proportions must match number of outputs");
		}
	
		double sum = 0.0;
		for (double p : proportions) {
			sum += p;
		}
		if (sum != 1.0) {
			throw new IllegalArgumentException("Proportions must sum to 1.0");
		}
		this.proportions = proportions;
	}

	@Override
	public void connect(Element elem, int index) {
		if (index >= 0 && index < outputs.length) {
			outputs[index] = elem;
		} else {
			throw new IndexOutOfBoundsException("Invalid output index: " + index);
		}
	}

	@Override
	public void simulate(SimulationObserver observer, double inFlow, boolean enableMaxFlowCheck) {
		if (enableMaxFlowCheck && maxFlow > 0 && inFlow > maxFlow) 
			observer.notifyFlowError("Multisplit", getName(), inFlow, maxFlow);
		

		double[] flows = new double[outputs.length];

	
		for(int i = 0; i < flows.length; i++) {
			flows[i] = inFlow * proportions[i];
		}

		observer.notifyFlow("Multisplit", getName(), inFlow, flows);

		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] != null) {
				outputs[i].simulate(observer, flows[i],enableMaxFlowCheck);
			}
		}

		
	}
	
}
