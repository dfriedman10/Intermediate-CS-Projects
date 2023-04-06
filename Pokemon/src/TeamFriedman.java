import java.util.ArrayList;

public class TeamFriedman extends PokeTeam {

	public TeamFriedman() {
		super("Team Friedman");
	}

	@Override
	public ArrayList<Pokemon> createTeam() {
		ArrayList<Pokemon> team = new ArrayList<Pokemon>();
		team.add(new Charmander());
		team.add(new Squirtle());
		team.add(new MrFriedman());

		return team;
	}

}
