import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LocationGraph<E> {
	
	HashMap<E, Vertex> vertices;
	
	public LocationGraph() {
		vertices = new HashMap<E, Vertex>();
	}
	
	public void addVertex(E info, int x, int y) {
		vertices.put(info, new Vertex(info, x, y));
	}
	
	public void connect(E info1, E info2) {
		Vertex v1 = vertices.get(info1);
		Vertex v2 = vertices.get(info2);
		
		if (v1 == null || v2 == null) {
			System.out.println("Vertex " + (v1==null? v1:v2).toString() + " not found");
			return;
		}
		
		Edge e = new Edge(v1, v2);
		
		v1.edges.add(e);
		v2.edges.add(e);
	}

	public class Edge {
		Vertex v1, v2;
		
		public Edge(Vertex v1, Vertex v2) {
			this.v1 = v1; this.v2 = v2;
		}
		
		public Vertex getNeighbor(Vertex v) {
			if (v.info.equals(v1.info)) {
				return v2;
			}
			return v1;
		}
		
	}
	
	public class Vertex {
		E info;
		HashSet<Edge> edges;
		int x, y;
		
		public Vertex(E info, int x, int y) {
			this.info = info;
			edges = new HashSet<Edge>();
			this.x = x;
			this.y = y;
		}
		
		public double distance(Vertex v2) {
			return Math.sqrt(Math.pow(x-v2.x, 2) + Math.pow(y-v2.y, 2));
		}
	}

	
	public ArrayList<Vertex> BFSsearch(E start, E target) {
		
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
		
		HashSet<Vertex> visited = new HashSet<Vertex>();
		visited.add(vertices.get(start));
		
		HashMap<Vertex, Edge> leadsTo = new HashMap<Vertex, Edge>();
		
		while (!toVisit.isEmpty()) {
			
			Vertex curr = toVisit.remove(0);
			
			for (Edge e : curr.edges) {
				
				Vertex neighbor = e.getNeighbor(curr);
				
				if (visited.contains(neighbor)) continue;
				

				leadsTo.put(neighbor, e);
				
				if (neighbor.info.equals(target)) {
					
					return backtrace(neighbor, leadsTo);
				}
				
				else {
					toVisit.add(neighbor);
					visited.add(neighbor);
				}
			}
		}
		return null;
	}
	
	public ArrayList<Vertex> dijkstraSearch(E start, E target) {
		Vertex startVertex = vertices.get(start);
		
		if (startVertex == null) {
			System.out.println(start + " is not in the graph");
			return null;
		}		
		if (vertices.get(target) == null) {
			System.out.println(target + " is not in the graph");
			return null;
		}
		
		HashMap<Vertex, Double> distances = new HashMap<Vertex, Double>();
		HashMap<Vertex, Edge> leadsTo = new HashMap<Vertex, Edge>();
		HashSet<Vertex> visited = new HashSet<Vertex>();
		PriorityQueue<Vertex> toVisit = new PriorityQueue<Vertex>();
		toVisit.add(vertices.get(start), 0);
		
		for (Vertex v : vertices.values()) {
			distances.put(v,Double.MAX_VALUE);
		}
		distances.put(startVertex, 0.0);
		
		while (toVisit.size() > 0) {
			Vertex curr = toVisit.pop();
			visited.add(curr);
			
			if (curr.info.equals(target)) {
				return backtrace(curr, leadsTo);
			}
			
			for (Edge e : curr.edges) {
				Vertex neighbor = e.getNeighbor(curr);
				
				if (visited.contains(neighbor))
					continue;
				
				double dist = distances.get(curr) + curr.distance(neighbor);
				
				if (dist < distances.get(neighbor)) {
					distances.put(neighbor,dist);
					leadsTo.put(neighbor, e);
					
					toVisit.add(neighbor, dist);
				}
			}
		}
		return null;
	}
	
	public ArrayList<Vertex> backtrace(Vertex target, HashMap<Vertex, Edge> leadsTo) {
		
		Vertex curr = target;
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		
		while (leadsTo.get(curr) != null) {
			path.add(0, curr);
			curr = leadsTo.get(curr).getNeighbor(curr);
		}
		path.add(0, curr);
		return path;
		
	}
	
	public static void main(String[] args) {
		LocationGraph<String> g = new LocationGraph<String>();
		
		g.addVertex("A", 100, 100);
		g.addVertex("B", 1000, 100);
		g.addVertex("C", 600, 300);
		g.addVertex("D", 100, 300);
		g.addVertex("E", 300, 300);
		
		g.connect("A", "B");
		g.connect("A", "D");
		g.connect("B", "C");
		g.connect("D", "E");
		g.connect("E", "C");
		
		for (LocationGraph.Vertex v : g.dijkstraSearch("A", "C")) 
			System.out.println(v.info);
		
	}
}
