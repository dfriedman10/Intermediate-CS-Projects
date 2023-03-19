
import java.util.ArrayList;

public class PriorityQueue {
	
	// a queue with priority least to greatest.
	
	private ArrayList<Node> queue;
	
	public PriorityQueue() {
		queue = new ArrayList<Node>();
	}
	
	public void add(Node n) {
		for (int i = 0; i < queue.size(); i++) 
			if (n.getFreq() < queue.get(i).getFreq()) {
				queue.add(i, n);
				return;
			}
		queue.add(n);
	}
	
	public String toString() {
		return queue.toString();
	}
	
	public Node pop() {
		Node n = queue.get(0);
		queue.remove(0);
		return n;
	}

	public int size() {
		return queue.size();
	}
}
