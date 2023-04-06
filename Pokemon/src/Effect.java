import java.awt.Color;

public enum Effect {
	
	NONE("", "", Color.white, 0), ASLEEP("ASP", "put to sleep", Color.gray, 0), POISONED("PSN", "poisoned", new Color(200,0,250), 7), 
	CONFUSED("CNF", "confused", Color.red, 15), PARALYZED("PAR", "paralyzed", Color.orange, 0);
	
	public final String message, label; 
	public final Color color;
	public final int damage;
	
	private Effect(String label, String message, Color color, int damage) {
		this.message = message;
		this.label = label;
		this.color = color;
		this.damage = damage;
	}
}
