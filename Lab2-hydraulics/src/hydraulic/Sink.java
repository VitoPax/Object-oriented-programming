package hydraulic;

/**
 * Represents the sink, i.e. the terminal element of a system
 *
 */
public class Sink extends Element {

	/**
	 * Constructor
	 * @param name name of the sink element
	 */
	public Sink(String name) {
		super(name);
	}

	@Override
    public void connect(Element e) {
        // no-op
    }

    @Override
    public void simulate(SimulationObserver observer, double inFlow, boolean enableMaxFlowCheck) {
		if (enableMaxFlowCheck && maxFlow > 0 && inFlow > maxFlow) 
			observer.notifyFlowError("Sink", name, inFlow, maxFlow);
			

        observer.notifyFlow("Sink", name, inFlow, SimulationObserver.NO_FLOW);
    }


}
