import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LabeledGraph<E, T> {
	
	HashMap<E, Vertex> vertices;
	
	public LabeledGraph() {
		vertices = new HashMap<E, Vertex>();
	}
	
	public void addVertex(E info) {
		vertices.put(info, new Vertex(info));
	}
	
	public void connect(E info1, E info2, T label) {
		Vertex v1 = vertices.get(info1);
		Vertex v2 = vertices.get(info2);
		
		if (v1 == null || v2 == null) {
			System.out.println("Vertex " + (v1==null? v1:v2).toString() + " not found");
			return;
		}
		
		Edge e = new Edge(label, v1, v2);
		
		v1.edges.add(e);
		v2.edges.add(e);
	}

	private class Edge {
		T label;
		Vertex v1, v2;
		
		public Edge(T label, Vertex v1, Vertex v2) {
			this.label = label; this.v1 = v1; this.v2 = v2;
		}
		
		public Vertex getNeighbor(Vertex v) {
			if (v.info.equals(v1.info)) {
				return v2;
			}
			return v1;
		}
		
	}
	
	class Vertex {
		E info;
		HashSet<Edge> edges;
		
		public Vertex(E info) {
			this.info = info;
			edges = new HashSet<Edge>();
		}
	}
	
	
	public ArrayList<Object> BFS(E start, E target) {
		
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
	
	public ArrayList<Object> backtrace(Vertex target, HashMap<Vertex, Edge> leadsTo) {
		
		Vertex curr = target;
		ArrayList<Object> path = new ArrayList<Object>();
		
		while (leadsTo.get(curr) != null) {
			path.add(0, curr.info);
			path.add(0, leadsTo.get(curr).label);
			curr = leadsTo.get(curr).getNeighbor(curr);
		}
		path.add(0, curr.info);
		return path;
		
	}
	
	public ArrayList<Object> furthest(E start) {
		if (vertices.get(start) == null) {
			System.out.println("Vertex " + start.toString() + " not found");
			return null;
		}
		
		ArrayList<Vertex> toVisit = new ArrayList<Vertex>();
		toVisit.add(vertices.get(start));
		
		HashSet<Vertex> visited = new HashSet<Vertex>();
		visited.add(vertices.get(start));
		
		HashMap<Vertex, Edge> leadsTo = new HashMap<Vertex, Edge>();
		
		Vertex curr = vertices.get(start);
		
		while (!toVisit.isEmpty()) {
			
			curr = toVisit.remove(0);
			
			for (Edge e : curr.edges) {
				
				Vertex neighbor = e.getNeighbor(curr);
				
				if (visited.contains(neighbor)) continue;
				

				leadsTo.put(neighbor, e);
		

				toVisit.add(neighbor);
				visited.add(neighbor);
				
			}
		}
		
		System.out.println("Furthest vertex: " + curr.info.toString());
		
		return backtrace(curr, leadsTo);
	}
	
	public double averageConnectivity(E start) {
		if (vertices.get(start) == null) {
			System.out.println("Vertex " + start.toString() + " not found");
			return -1;
		}
		
		ArrayList<Vertex> toVisit = new ArrayList<Vertex>();
		toVisit.add(vertices.get(start));
		
		HashSet<Vertex> visited = new HashSet<Vertex>();
		visited.add(vertices.get(start));
		
		HashMap<Vertex, Integer> distanceFromStart = new HashMap<Vertex, Integer>();
		
		Vertex curr = vertices.get(start);
		distanceFromStart.put(curr, 0);
		double totalDist = 0;
		
		while (!toVisit.isEmpty()) {
			
			curr = toVisit.remove(0);
			
			for (Edge e : curr.edges) {
				
				Vertex neighbor = e.getNeighbor(curr);
				
				if (visited.contains(neighbor)) continue;
				

				distanceFromStart.put(neighbor, distanceFromStart.get(curr)+1);
				totalDist += distanceFromStart.get(neighbor);
		

				toVisit.add(neighbor);
				visited.add(neighbor);
				
			}
		}
		if (visited.size() == 1) {
			System.out.println("Not connected to any actors");
			return -1;
		}
		
		double avgDist = totalDist/(visited.size()-1);
		
		System.out.println("Avg Connectivity: " + avgDist);
		
		return avgDist;
	}
	
	public ArrayList<Object> mostEdgesBetween(E start) {

		Vertex v = vertices.get(start);
		
		if (v == null) {
			System.out.println("Vertex " + start.toString() + " not found");
			return null;
		}
		
		if (v.edges.size() == 0) {
			System.out.println("Vertex " + start.toString() + " has no connections");
			return null;
		}
		
		HashMap<Vertex, ArrayList<Object>> connections = new HashMap<Vertex, ArrayList<Object>>();
		
		for (Edge edge : v.edges) {
			Vertex neighbor = (edge.getNeighbor(v));
			if (!connections.containsKey(neighbor)) 
				connections.put(neighbor, new ArrayList<Object>());	
			
			connections.get(neighbor).add(edge.label);
		}
		
		Vertex maxVertex = null;
		int maxEdges = 0;
		
		for (Vertex neighbor : connections.keySet()) {
			if (connections.get(neighbor).size() > maxEdges) {
				maxEdges = connections.get(neighbor).size();
				maxVertex = neighbor;
			}
		}
		connections.get(maxVertex).add(0, maxVertex.info);
		return connections.get(maxVertex);
	}
	
	public static void main(String[] args) {
		
		LabeledGraph<String, String> g = new LabeledGraph<String, String>();
		g.addVertex("Bob");
		g.addVertex("Sue");
		g.addVertex("Jim");
		g.addVertex("Bill");
		g.connect("Bob", "Sue", "friends");
		g.connect("Sue", "Jim", "friends");
		g.connect("Sue", "Bill", "friends");
		System.out.println(g.averageConnectivity("Bob"));
		
	}
}
