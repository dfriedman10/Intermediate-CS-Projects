import java.awt.Image;

import javax.swing.ImageIcon;

public enum Type {

	FIRE("fire.png", 1), WATER("water.png", 2), NORMAL("normal.png", 0), GRASS("grass.png", 3);

	public final Image img;
	public final int effectivenessIndex;
	
	private Type(String imgName, int effectivenessIndex) {
		img = new ImageIcon(getClass().getClassLoader().getResource("resources/"+imgName)).getImage();			
		this.effectivenessIndex = effectivenessIndex;
	}
	
}
