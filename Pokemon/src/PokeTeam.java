

import java.util.ArrayList;

// holds 3 pokemon for a student's team

public abstract class PokeTeam {

	private ArrayList<Pokemon> team;
	private final String name;
	
	// max legal score that any team can sum to
	public static final int SCORELIMIT = 1050;
	
	public PokeTeam(String name) {
		this.team = createTeam(); this.name = name;
	}
	
	// generates the list of 3 pokemon
	public abstract ArrayList<Pokemon> createTeam();
	
	public String getName() {return name;}
	public ArrayList<Pokemon> getTeam() {return team;}
	
	public boolean isAlive() {return !team.isEmpty();}
	
	// sums the 3 pokemon's scores in the team, checking for team legality.
	// each pokemon must have 4 moves and no new methods
	// the team must have 3 pokemon and the score sum cannot exceed SCORELIMIT
	public boolean isLegal() { 
		int score = 0; 
		for (Pokemon p : team) {
			score += p.score();
			if (p.moves.length != 4 || p.getClass().getDeclaredMethods().length!=1)
				return false;
		}
		return score <= SCORELIMIT && team.size() == 3;
		
	}
}
