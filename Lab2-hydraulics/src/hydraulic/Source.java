package hydraulic;

/**
 * Represents a source of water, i.e. the initial element for the simulation.
 *
 * Lo status of the source is defined through the method
 * {@link #setFlow(double) setFlow()}.
 */
public class Source extends Element {

	private double flow;
    private Element output;

	/**  
	 * constructor
	 * @param name name of the source element
	 */
	public Source(String name) {
		super(name);
	}

	/**
	 * Define the flow of the source to be used during the simulation
	 *
	 * @param flow flow of the source (in cubic meters per hour)
	 */
	public void setFlow(double flow){
		this.flow = flow;
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

        observer.notifyFlow("Source", name, SimulationObserver.NO_FLOW, flow);
        
		if (output != null)
            output.simulate(observer, flow, enableMaxFlowCheck);
    }


}
