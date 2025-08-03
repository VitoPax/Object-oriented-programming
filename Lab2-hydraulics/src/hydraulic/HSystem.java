package hydraulic;

/**
 * Main class that acts as a container of the elements for
 * the simulation of an hydraulics system 
 * 
 */
public class HSystem {

	private static final int MAX_ELEMENTS = 100;
	private Element[] elements = new Element[MAX_ELEMENTS];
	private int count;

// R1
	/**
	 * Adds a new element to the system
	 * 
	 * @param elem the new element to be added to the system
	 */
	public void addElement(Element elem){
		if (count < MAX_ELEMENTS) {
            elements[count++] = elem;
        }
	}

	/**
	 * returns the number of element currently present in the system
	 * 
	 * @return count of elements
	 */
	public int size() {
		return count;
    }

	/**
	 * returns the element added so far to the system
	 * 
	 * @return an array of elements whose length is equal to 
	 * 							the number of added elements
	 */
	public Element[] getElements() {
		Element[] result = new Element[count];
       	System.arraycopy(elements, 0, result, 0, count);
        return result;
	}

// R4
	/**
	 * starts the simulation of the system
	 * 
	 * The notification about the simulations are sent
	 * to an observer object
	 * 
	 * Before starting simulation the parameters of the
	 * elements of the system must be defined
	 * 
	 * @param observer the observer receiving notifications
	 */
	public void simulate(SimulationObserver observer){
		for (int i = 0; i < count; i++) {
			if (elements[i] instanceof Source) {
				elements[i].simulate(observer,SimulationObserver.NO_FLOW,false);
			}
		}
	}


// R6
	/**
	 * Deletes a previously added element 
	 * with the given name from the system
	 */
	public boolean deleteElement(String name) {
		Element toDelete = null;
		int index = -1;

		// Search for the element to delete
		for (int i = 0; i < count; i++) {
			if (elements[i].getName().equals(name)) {
				toDelete = elements[i];
				index = i;
				break;
			}
		}

		if (toDelete == null) {
			return false; // Element not found
		}

		// Check if it's a Split or Multisplit with more than one connected output
		if (toDelete instanceof Split || toDelete instanceof Multisplit) {
			Element[] outputs = toDelete.getOutputs();
			int connected = 0;
			for (Element out : outputs) {
				if (out != null) connected++;
			}
			if (connected > 1) {
				return false; // Cannot remove: multiple outputs are connected
			}
		}

		// Identify the upstream element (the one that connects to this element)
		Element upstream = null;
		int upstreamIndex = -1;
		for (Element e : elements) {
			if (e == null || e == toDelete) continue;

			Element[] outputs = e.getOutputs();
			if (outputs != null) {
				for (int i = 0; i < outputs.length; i++) {
					if (outputs[i] == toDelete) {
						upstream = e;
						upstreamIndex = i;
						break;
					}
				}
			} else {
				if (e.getOutput() == toDelete) {
					upstream = e;
					break;
				}
			}
		}

		// Identify the downstream element (the one that this element connects to)
		Element[] outElements = toDelete.getOutputs();
		Element downstream = null;
		if (outElements != null) {
			for (Element out : outElements) {
				if (out != null) {
					downstream = out;
					break;
				}
			}
		} else {
			downstream = toDelete.getOutput();
		}

		// Reconnect upstream to downstream
		if (upstream != null) {
			if (upstream.getOutputs() != null) {
				upstream.connect(downstream, upstreamIndex);
			} else {
				upstream.connect(downstream);
			}
		}

		// Remove the element from the elements array
		for (int i = index; i < count - 1; i++) {
			elements[i] = elements[i + 1];
		}
		elements[--count] = null;

		return true;
		
}


// R7
	/**
	 * starts the simulation of the system; if {@code enableMaxFlowCheck} is {@code true},
	 * checks also the elements maximum flows against the input flow
	 * 
	 * If {@code enableMaxFlowCheck} is {@code false}  a normals simulation as
	 * the method {@link #simulate(SimulationObserver)} is performed
	 * 
	 * Before performing a checked simulation the max flows of the elements in thes
	 * system must be defined.
	 */
	public void simulate(SimulationObserver observer, boolean enableMaxFlowCheck) {
		for (int i = 0; i < count; i++) {
			if (elements[i] instanceof Source) {
				elements[i].simulate(observer, SimulationObserver.NO_FLOW, enableMaxFlowCheck);
			}
   		}
	
	}

// R8
	/**
	 * creates a new builder that can be used to create a 
	 * hydraulic system through a fluent API 
	 * 
	 * @return the builder object
	 */
    public static HBuilder build() {
		return new HBuilder();
    }
}
