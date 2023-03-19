
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class KevinBacon {
	
	public static final int WIDTH = 500, HEIGHT = 600;
	
	public static ArrayList<String> actorSearch(String actor1, String actor2, LabeledGraph<String, String> graph) {
		ArrayList<String> result = new ArrayList<String>();
		if (!graph.vertices.containsKey(actor1.toLowerCase())) 
			result.add(capitalize(actor1) + " is not in the database. "
					+ "Try another name, and make sure to spell correctly.");
		
		else {
			
			if (!graph.vertices.containsKey(actor2.toLowerCase())) 
				result.add(capitalize(actor2) + " is not in the database. "
						+ "Try another name, and make sure to spell correctly.");
				
			else {
				ArrayList<Object> path = graph.BFS(actor1.toLowerCase(), actor2.toLowerCase());
				if (path == null)
					result.add("No connection between " + capitalize(actor1) + " and " + capitalize(actor2));
				else if (path.size() == 1)
					result.add(actor1 + "'s " + actor1 + " number is 0.");
				else {
					result.add(capitalize((String)path.get(path.size()-1)) + "'s " + capitalize((String)path.get(0)) + " number is " + path.size()/2 + "\n\n");
					for (int i = 0; i < path.size()-1; i+=2) 
						result.add("    " + capitalize((String)path.get(i)) + " was in " + path.get(i+1) + " with " + capitalize((String)path.get(i+2)) + "\n\n");
				}
			}
		}
		return result;
	}
	
	public static ArrayList<String> furthestActor(String actor, LabeledGraph<String, String> graph) {
		ArrayList<String> result = new ArrayList<String>();
		if (!graph.vertices.containsKey(actor.toLowerCase())) 
			result.add(capitalize(actor) + " is not in the database. "
					+ "Try another name, and make sure to spell correctly.");
				
		else {
			ArrayList<Object> path = graph.furthest(actor.toLowerCase());
			if (path.size() == 1)
				result.add(capitalize(actor) + " is not connected to any other actors.");
			else {
				result.add("The furthest actor from "+capitalize((String)path.get(0)) + " is " + capitalize((String)path.get(path.size()-1))+ ", with a distance of "
						+ path.size()/2+ "\n\n");
				for (int i = 0; i < path.size()-1; i+=2) 
					result.add("    " + capitalize((String)path.get(i)) + " was in " + path.get(i+1) + " with " + capitalize((String)path.get(i+2)) + "\n\n");
			}
		}
		return result;
	}
	
	public static ArrayList<String> mostConnected(String actor, LabeledGraph<String, String> graph) {
		ArrayList<String> result = new ArrayList<String>();
		if (!graph.vertices.containsKey(actor.toLowerCase())) 
			result.add(capitalize(actor) + " is not in the database. "
					+ "Try another name, and make sure to spell correctly.");
				
		else {
			ArrayList<Object> path = graph.mostEdgesBetween(actor.toLowerCase());
			if (path == null)
				result.add(capitalize(actor) + " is not connected to any other actors.");
			else {
				
				result.add("The actor with the most direct connections to "+capitalize(actor) + " is " + capitalize((String)path.get(0))+ ", appearing in "
						+ path.size() + " movies together:"+ "\n\n");
				for (int i = 1; i < path.size(); i++) 
					result.add("    " + path.get(i) + "\n\n");
			}
		}
		return result;
	}
	
	public static String avgConnectivity(String actor, LabeledGraph<String, String> graph) {
		String result = "";
		if (!graph.vertices.containsKey(actor.toLowerCase())) 
			result = (capitalize(actor) + " is not in the database. "
					+ "Try another name, and make sure to spell correctly.");
				
		else {
			double avgDist = graph.averageConnectivity(actor.toLowerCase());
			if (avgDist == -1)
				result = (capitalize(actor) + " is not connected to any other actors.");
			else {
				result = ("The average connectivity to "+ capitalize(actor) + " is " + avgDist + "\n\n");
			}
		}
		return result;
	}
	
	public static LabeledGraph<String, String> loadFiles() throws IOException {
		LabeledGraph<String, String> graph = new LabeledGraph<String, String>();
		
		BufferedReader actorsIn = new BufferedReader(new FileReader("actors.txt"));
		BufferedReader maIn = new BufferedReader(new FileReader("movie-actors.txt"));
		BufferedReader moviesIn = new BufferedReader(new FileReader("movies.txt"));
		
		HashMap<Integer, String> actorCodes = new HashMap<Integer, String>();
		HashMap<Integer, String> movieCodes = new HashMap<Integer, String>();
		
		for (String line = actorsIn.readLine(); line != null && line.trim().length() != 0; line = actorsIn.readLine()) {
			String[] splitLine = line.split("~");
			actorCodes.put(Integer.parseInt(splitLine[0]), splitLine[1].toLowerCase());
			graph.addVertex(splitLine[1].toLowerCase());
		}
		
		for (String line = moviesIn.readLine(); line != null && line.trim().length() != 0; line = moviesIn.readLine()) {
			String[] splitLine = line.split("~");
			movieCodes.put(Integer.parseInt(splitLine[0]), splitLine[1]);
		}
		int edgeCount = 0;
		for (String line = maIn.readLine(); line != null && line.trim().length() != 0;) {
			String movieCode = line.split("~")[0];
			ArrayList<String> movieActors = new ArrayList<String>();
			for (;line != null && line.trim().length() != 0 && movieCode.equals(line.split("~")[0]); line = maIn.readLine()) 
				movieActors.add(actorCodes.get(Integer.parseInt(line.split("~")[1].toLowerCase())));
			
			for (int i = 0; i < movieActors.size(); i++) 
				for (int j = i+1; j < movieActors.size(); j++)  {
					graph.connect(movieActors.get(i), movieActors.get(j), movieCodes.get(Integer.parseInt(movieCode)));
					edgeCount++;
				}
			
		}
		System.out.println(edgeCount);
		actorsIn.close();
		moviesIn.close();
		maIn.close();
		System.out.println(graph.vertices.size());
		System.out.println(graph.vertices.size());
		return graph;
	}
	
	public static String capitalize(String string) {
		  char[] chars = string.toLowerCase().toCharArray();
		  boolean found = false;
		  for (int i = 0; i < chars.length; i++) {
		    if (!found && Character.isLetter(chars[i])) {
		      chars[i] = Character.toUpperCase(chars[i]);
		      found = true;
		    } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
		      found = false;
		    }
		  }
		  return String.valueOf(chars);
		}
	
	public static void main(String[] args) throws IOException {
		loadFiles();
	}
}
