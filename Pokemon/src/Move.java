
// class to represent a pokemon's move

public class Move {

	public final Type type;
	public final int damage;
	public final Effect effect;
	public final String name;
	
	public Move(String name, Type type, int dam, Effect eff) {
		this.name = name; this.type = type;
		damage = dam; effect = eff;
	}
	
	
}
