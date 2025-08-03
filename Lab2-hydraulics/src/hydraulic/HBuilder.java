package hydraulic;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Hydraulics system builder providing a fluent API
 */
public class HBuilder {

    private Source source = null;
    private List<Element> elements = new LinkedList<>();
    private Element lastElement = null;

    private final Deque<Split> splitStack = new LinkedList<>();
    private final Deque<Integer> indexStack = new LinkedList<>();

    /**
     * Add a source element with the given name
     * 
     * @param name name of the source element to be added
     * @return the builder itself for chaining 
     */
    public HBuilder addSource(String name) {
        this.source = new Source(name);
        if (lastElement != null) {
            source.connect(lastElement);
        } else {
            lastElement = source;
        }
        return this;
    }

    /**
     * returns the hydraulic system built with the previous operations
     * 
     * @return the hydraulic system
     */
    public HSystem complete() {
        HSystem system = new HSystem();
        if (source != null) {
            system.addElement(source);
        }
        elements.forEach(system::addElement);
        return system;
    }

    private void testAddLast(Element element) {
        if (!splitStack.isEmpty()) {
            Split split = splitStack.peek();
            int index = indexStack.peek();
            if (index >= split.outputs.length) {
                throw new IllegalStateException("No more outputs to connect to.");
            }

            if (split.outputs[index] == null) {
                split.connect(element, index);
            } else if (lastElement != null) {
                lastElement.connect(element);
            }
        }else if (lastElement != null) {
            lastElement.connect(element);
        }
        lastElement = element;
        elements.add(element);
    }

    /**
     * creates a new tap and links it to the previous element
     * 
     * @param name name of the tap
     * @return the builder itself for chaining 
     */
    public HBuilder linkToTap(String name) {
        Tap tap = new Tap(name);
        testAddLast(tap);
        return this;
    }

    /**
     * creates a sink and link it to the previous element
     * @param name name of the sink
     * @return the builder itself for chaining 
     */
    public HBuilder linkToSink(String name) {
        Sink sink = new Sink(name);
        testAddLast(sink);
        return this;
    }

    /**
     * creates a split and links it to the previous element
     * @param name of the split
     * @return the builder itself for chaining 
     */
    public HBuilder linkToSplit(String name) {
        Split split = new Split(name);
        testAddLast(split);
        return this;
    }

    /**
     * creates a multisplit and links it to the previous element
     * @param name name of the multisplit
     * @param numOutput the number of output of the multisplit
     * @return the builder itself for chaining 
     */
    public HBuilder linkToMultisplit(String name, int numOutput) {
        Multisplit multisplit = new Multisplit(name, numOutput);
        testAddLast(multisplit);
        return this;
    }

    /**
     * introduces the elements connected to the first output 
     * of the latest split/multisplit.
     * The element connected to the following outputs are 
     * introduced by {@link #then()}.
     * 
     * @return the builder itself for chaining 
     */
    public HBuilder withOutputs() {
        if (lastElement instanceof Split) {
            splitStack.push((Split) lastElement);
            indexStack.push(0);
        } else {
            throw new IllegalStateException("No split or multisplit to connect outputs to.");
        }
        return this;     
    }

    /**
     * inform the builder that the next element will be
     * linked to the successive output of the previous split or multisplit.
     * The element connected to the first output is
     * introduced by {@link #withOutputs()}.
     * 
     * @return the builder itself for chaining 
     */
    public HBuilder then() {
        if (splitStack.isEmpty()) {
            throw new IllegalStateException("No split or multisplit to connect outputs to.");
        }

        indexStack.push(indexStack.pop() + 1);

        return this;
    }

    /**
     * completes the definition of elements connected
     * to outputs of a split/multisplit. 
     * 
     * @return the builder itself for chaining 
     */
    public HBuilder done() {
        if (splitStack.isEmpty()) {
            throw new IllegalStateException("No split or multisplit to complete.");
        }
        splitStack.pop();
        indexStack.pop();
        return this;
    }

    /**
     * define the flow of the previous source
     * 
     * @param flow flow used in the simulation
     * @return the builder itself for chaining 
     */
    public HBuilder withFlow(double flow) {
        if (source != null) {
            source.setFlow(flow);
        } else {
            throw new IllegalStateException("No source to set flow for.");
        }
        return this;
    }

    /**
     * define the status of a tap as open,
     * it will be used in the simulation
     * 
     * @return the builder itself for chaining 
     */
    public HBuilder open() {
        if (lastElement instanceof Tap) {
            ((Tap) lastElement).setOpen(true);
        } else {
            throw new IllegalStateException("Last element is not a tap.");
        }
        return this;
    }

    /**
     * define the status of a tap as closed,
     * it will be used in the simulation
     * 
     * @return the builder itself for chaining 
     */
    public HBuilder closed() {
        if (lastElement instanceof Tap) {
            ((Tap) lastElement).setOpen(false);
        } else {
            throw new IllegalStateException("Last element is not a tap.");
        }
        return this;
    }

    /**
     * define the proportions of input flow distributed
     * to each output of the preceding a multisplit
     * 
     * @param props the proportions
     * @return the builder itself for chaining 
     */
    public HBuilder withPropotions(double[] props) {
        if (lastElement instanceof Multisplit) {
            ((Multisplit) lastElement).setProportions(props);
        } else {
            throw new IllegalStateException("Last element is not a multisplit.");
        }
        return this;
    }

    /**
     * define the maximum flow theshold for the previous element
     * 
     * @param max flow threshold
     * @return the builder itself for chaining 
     */
    public HBuilder maxFlow(double max) {
        if (lastElement != null) {
            lastElement.setMaxFlow(max);
        } else {
            throw new IllegalStateException("No element to set max flow for.");
        }
        return this;
    }
}
