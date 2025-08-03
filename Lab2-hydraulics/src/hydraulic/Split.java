package hydraulic;

/**
 * Represents a split element, a.k.a. T element
 * 
 * During the simulation each downstream element will
 * receive a stream that is half the input stream of the split.
 */

public class Split extends Element {

	protected Element[] outputs = new Element[2]; 

	/**
	 * Constructor
	 * @param name name of the split element
	 */
	public Split(String name) {
		super(name);
	}

 	@Override
    public void connect(Element elem, int index) {
        if (index == 0 || index == 1) {
            outputs[index] = elem;
        }
    }

    @Override
    public Element[] getOutputs() {
        return outputs;
    }

    @Override
    public void simulate(SimulationObserver observer, double inFlow, boolean enableMaxFlowCheck) {
		if (enableMaxFlowCheck && maxFlow > 0 && inFlow > maxFlow) 
			observer.notifyFlowError("Split", getName(), inFlow, maxFlow);
			
		
			double outFlow = inFlow / 2.0;

        observer.notifyFlow("Split", getName(), inFlow, outFlow, outFlow);

        for (int i = 0; i < 2; i++) {
            if (outputs[i] != null) {
                outputs[i].simulate(observer,outFlow, enableMaxFlowCheck);
            }
        }
    
    }

	
}
