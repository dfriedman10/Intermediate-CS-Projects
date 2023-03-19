import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class UnlabeledGraph<E> {
	
	private HashMap<E, Vertex> vertices;
	
	public UnlabeledGraph() {
		vertices = new HashMap<E, Vertex>();
	}
	
	public void addVertex(E info) {
		vertices.put(info, new Vertex(info));
	}
	
	public void removeVertex(E info) {
		
		for (Vertex v : vertices.get(info).neighbors) {
			v.neighbors.remove(vertices.get(info));
		}
		vertices.remove(info);
	}
	
	public void connect(E info1, E info2) {
		Vertex v1 = vertices.get(info1);
		Vertex v2 = vertices.get(info2);
		
		if (v1 == null || v2 == null) {
			throw(new NullPointerException());
		}
		
		v1.neighbors.add(v2);
		v2.neighbors.add(v1);
	}
	
	private class Vertex {
		E info;
		HashSet<Vertex> neighbors;
		
		public Vertex(E info) {
			this.info = info;
			neighbors = new HashSet<Vertex>();
		}
	}
	
	
	public ArrayList<E> BFS(E start, E target) {
		
		if (vertices.get(start) == null) {
			System.out.println("Vertex " + start.toString() + " not found");
			return null;
		}
		if (vertices.get(target) == null) {
			System.out.println("Vertex " + target.toString() + " not found");
			return null;
		}
		
		ArrayList<Vertex> toVisit = new ArrayList<Vertex>();
		toVisit.add(vertices.get(start));
		
		HashMap<Vertex, Vertex> leadsTo = new HashMap<Vertex, Vertex>();
		leadsTo.put(vertices.get(start), null);
		
		while (!toVisit.isEmpty()) {
			
			Vertex curr = toVisit.remove(0);
			
			for (Vertex neighbor : curr.neighbors) {
				
				if (leadsTo.containsKey(neighbor)) continue;
				

				leadsTo.put(neighbor, curr);
				
				if (neighbor.info.equals(target)) {
					
					return backtrace(neighbor, leadsTo);
				}
				
				else {
					toVisit.add(neighbor);
				}
			}
		}
		return null;
	}
	
	public ArrayList<E> backtrace(Vertex target, HashMap<Vertex, Vertex> leadsTo) {
		
		Vertex curr = target;
		ArrayList<E> path = new ArrayList<E>();
		
		while (curr != null) {
			path.add(0, curr.info);
			curr = leadsTo.get(curr);
		}
		return path;
		
	}
	
	public static void main(String[] args) {
		
		UnlabeledGraph<String> g = new UnlabeledGraph<String>();
		
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addVertex("D");
		g.addVertex("E");
		g.addVertex("F");
		
		g.connect("A", "B");
		g.connect("B", "C");
		g.connect("C", "D");
		g.connect("C", "E");
		g.connect("E", "D");
		g.connect("E", "F");
		g.connect("F", "D");
		
		System.out.println(g.BFS("A", "D"));
		
		
	}
}
