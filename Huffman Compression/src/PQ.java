
import java.util.ArrayList;

public class PQ<T> {

	private ArrayList<Node> queue = new ArrayList<Node>();
	
	class Node {
		T info;
		int priority;
		
		public Node(T info, int p) {
			this.info = info;
			priority = p;
		}
		
		public String toString() {
			return info.toString();
		}
	}
	
	public void add(T data, int p) {
		
		Node n = new Node(data, p);
		
		for (int i = 0; i < queue.size(); i++) 
			if (p < queue.get(i).priority) {
				queue.add(i, n);
				return;
			}
		queue.add(n);
	}
	
	public int getPriority() {
		return queue.get(queue.size()-1).priority;
	}
	
	// takes the last item, assuming our pq 
	// is in descending order
	public T pop() {
		
		return queue.remove(queue.size() - 1).info;
	}
	
	public String toString() {
		return queue.toString();
	}
	
	public int size() {
		return queue.size();
	}
	
}
