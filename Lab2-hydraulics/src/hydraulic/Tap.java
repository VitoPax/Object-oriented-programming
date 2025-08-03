package hydraulic;

/**
 * Represents a tap that can interrupt the flow.
 * 
 * The status of the tap is defined by the method
 * {@link #setOpen(boolean) setOpen()}.
 */

public class Tap extends Element {

	private boolean open = false; // default status is closed
    private Element output;

	/**
	 * Constructor
	 * @param name name of the tap element
	 */
	public Tap(String name) {
		super(name);
	}

	/**
	 * Set whether the tap is open or not. The status is used during the simulation.
	 *
	 * @param open opening status of the tap
	 */
	public void setOpen(boolean open){
		this.open = open;
	}

	@Override
    public void connect(Element e) {
        this.output = e;
    }

    @Override
    public Element getOutput() {
        return output;
    }

    @Override
    public void simulate(SimulationObserver observer, double inFlow, boolean enableMaxFlowCheck) {
		if (enableMaxFlowCheck && maxFlow > 0 && inFlow > maxFlow) 
			observer.notifyFlowError("Tap", name, inFlow, maxFlow);
		     
		
		double outFlow = open ? inFlow : 0.0;
        observer.notifyFlow("Tap", name, inFlow, outFlow);
        if (output != null)
            output.simulate(observer, outFlow, enableMaxFlowCheck);
    }
	
}
