
import java.util.ArrayList;

public class PriorityQueue<E> {
	
	private ArrayList<Node> queue = new ArrayList<Node>();
	
	public class Node {

		public double priority;
		public E info;
		
		public Node(double p, E i) {
			priority = p;
			info=i;
		}
		
		public String toString() {
			return "(" + info + ", " + priority + ")";
		}
	}
	
	public int size() {
		return queue.size();
	}
	
	public String toString() {
		return queue.toString();
	}
	
	public E pop() {
		return queue.remove(queue.size()-1).info;
	}
	
	public void add(E info, double priority) {
		Node newNode = new Node(priority, info);
		
		for (int i = 0; i < queue.size(); i++) {
			if (queue.get(i).info == info) {
				queue.remove(i);
				break;
			}
		}
		
		if (queue.size() == 0) {
			queue.add(newNode);
		}
		
		else if (queue.get(0).priority < newNode.priority) {
			queue.add(0, newNode);
		}
		
		else if (queue.get(queue.size()-1).priority > newNode.priority) {
			queue.add(newNode);
		}
	
		else {
			
			int start = 0, end = queue.size()-1;
			
			while (start < end) {
				
				Node midpoint = queue.get((start+end)/2);
				
				if (midpoint.priority > newNode.priority) {
					start = (start+end)/2 + 1;
				}
				
				else {
					end = (start+end)/2;
				}
			}
			
			queue.add(start, newNode);
		}
	}

	public static void main(String[] args) {
		PriorityQueue<Character> myQ = new PriorityQueue<Character>();
		
		for (int i = 0; i< 100; i++) {
			myQ.add('g', (int)(Math.random()*100));
		}
		
		System.out.println(myQ);

	}

}
