

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public abstract class Pokemon {

	private final String name;
	
	private final Type type;
	
	// current status the pokemon is affected by
	private Effect status = Effect.NONE;

	
	// health represents current health, maxHealth is initial health for 
	// display purposes
	private int health;
	private final double maxHealth;
	
	private final Image img;
	
	public final Move[] moves;
	
	// status may prevent the pokemon from moving on its turn
	private boolean canMove = true;
	
	
	public Pokemon(String pokeName, Type type, int health, String imgName) {
		img = new ImageIcon(getClass().getClassLoader().getResource("resources/"+imgName)).getImage();
		name = pokeName; this.type = type; this.health = health; maxHealth = health;
		this.moves = generateMoves();
	}
	
	public String toString() {
		return (name+": "+score());
	}
	
	// calculates the pokemon's score, an arbitrary metric 
	// defined by Mr. Friedman 
	// score = health*2 + sum(movescore)
	// movescore = damage + 30 if move causes an effect
	public final int score() {
		int score = health*2;
		for (Move m : moves) {
			score += m.damage;
			score += m.effect==Effect.NONE?0:30;
		}
		return score;
	}
	
	// applies the current effect to the pokemon
	// called only at the start of the pokemon's turn
	public final String effects() {
		
		// message to be provided after effects are performed
		String output = "";
		
		// sleeping has a 33% chance to end, otherwise the 
		// pokemon cannot move
		if (status == Effect.ASLEEP) {
			if (Math.random() < .33) {
				output += name +" woke up!\n";
				status = Effect.NONE;
			}else {
				canMove = false;
				return name +" is asleep.\n";
			}
		}
		
		// poisoning lasts forever (or until it is replaced by a new effect)
		// applies damage every turn
		else if (status == Effect.POISONED) {
			output += name +" is hurt by poison.\n";
			takeDamage(status.damage);
			if (health == 0) {
				canMove = false;
				return output;
			}
		}
		
		// confusing has a 25% chance to end. otherwise the pokemon
		// has a 50% chance to perform its move and a 50% chance to 
		// hurt itself
		else if (status == Effect.CONFUSED) {
			if (Math.random() < .25) {
				output += name +" snapped out of confusion!\n";
				status = Effect.NONE;
			}
			output += name +" is confused.\n";
			if (Math.random() < .5) {
				output += name + " hurt itself in its confusion.\n";
				takeDamage(status.damage);
				canMove = false;
				return output;
			}
		}
		
		// paralysis lasts forever (or until it is replaced by a new effect)
		// has a 30% chance every turn of preventing the pokemon from moving
		else if (status == Effect.PARALYZED) {
			if (Math.random() < .3) {
				canMove = false;
				return name + " is paralyzed!\n";
			}
		}
	
		// if no effect 
		canMove = true;
		return output;
	}

	// randomly chooses one of the Pokemon's 4 moves
	// unless it can't move this turn
	public final Move attack() {
		if (!canMove) return null;
		
		return moves[(int)(Math.random()*4)];
	}

	// receives damage/effect from an opponent's move
	public final String suffer(Move move) {
		
		// message to be provided after damage/effect are dealt
		String output = "";
		
		// determines the effectiveness of this move. 
		// for example, a water attack would have a 
		// multiplier of 2 when used against a fire type pokemon
		double multiplier = PokeBattleRunner.effectiveness
				[move.type.effectivenessIndex][this.type.effectivenessIndex];
		
		if (multiplier == .5)
			output += "Not very effective...\n";
		else if (multiplier == 2)
			output += "super effective!\n";
		this.takeDamage((int)(move.damage*multiplier));
		
		// applies the move's effect to this pokemon
		if (move.effect != Effect.NONE) {
			this.setStatus(move.effect);
			output += this.name + " was " + move.effect.message;
		}
		
		return output;
	}
	
	// applies damage to our pokemon, with a floor of 0
	public final void takeDamage(int dam) {
		health = Math.max(health-dam, 0);
	}
	
	public int getHealth() {return health;}
	public Type getType() {return type;}
	public String getName() {return name;}
	
	private void setStatus(Effect status) {
		this.status = status;
	}
	
	// to be filled in by subclass. Fills in the moves array with 4 moves
	public abstract Move[] generateMoves();
	
	// displays our pokemon. location depends on whether the pokemon is in the 
	// top right corner or bottom left. 
	public final void draw(Graphics g, boolean top) {
		if (!top) {
			if (health > 0)
				g.drawImage(img, PokeBattleRunner.POKEX[0], PokeBattleRunner.POKEY[0], PokeBattleRunner.POKEWIDTH, PokeBattleRunner.POKEHEIGHT, null);
			if (health/maxHealth >= .5)
				g.setColor(Color.GREEN);
			else if (health/maxHealth >= .25)
				g.setColor(Color.orange);
			else
				g.setColor(Color.red);
			g.fillRect((int)(PokeBattleRunner.WIDTH/1.3), (int)(PokeBattleRunner.HEIGHT/1.64), (int)(PokeBattleRunner.HEALTHWIDTH*(health/maxHealth)), PokeBattleRunner.HEALTHHEIGHT);
			g.setColor(Color.black);
			g.setFont(new Font("Courier",Font.PLAIN, PokeBattleRunner.HEIGHT/20));
			g.drawImage(type.img,PokeBattleRunner.WIDTH*35/60,PokeBattleRunner.HEIGHT*22/40, PokeBattleRunner.WIDTH/30, PokeBattleRunner.HEIGHT/30, null);
			g.drawString(name, (int)(PokeBattleRunner.WIDTH*37/60 ), (int)(PokeBattleRunner.HEIGHT*23/40));
			g.setColor(status.color);
			g.drawString(status.label, (int)(PokeBattleRunner.WIDTH*3/5 ), (int)(PokeBattleRunner.HEIGHT*25/40));
		}
		else {
			if (health > 0)
				g.drawImage(img, PokeBattleRunner.POKEX[1], PokeBattleRunner.POKEY[1], PokeBattleRunner.POKEWIDTH, PokeBattleRunner.POKEHEIGHT, null);
			if (health/maxHealth >= .5)
				g.setColor(Color.GREEN);
			else if (health/maxHealth >= .25)
				g.setColor(Color.orange);
			else
				g.setColor(Color.red);
			g.fillRect((int)(PokeBattleRunner.WIDTH/5.22), (int)(PokeBattleRunner.HEIGHT/4.65), (int)(PokeBattleRunner.HEALTHWIDTH*(health/maxHealth)), PokeBattleRunner.HEALTHHEIGHT);
			g.setColor(Color.black);
			g.setFont(new Font("Courier",Font.PLAIN, PokeBattleRunner.HEIGHT/20));
			g.drawImage(type.img,(int)(PokeBattleRunner.WIDTH/200 ), (int)(PokeBattleRunner.HEIGHT*11/80), PokeBattleRunner.WIDTH/30, PokeBattleRunner.HEIGHT/30, null);
			g.drawString(name, (int)(PokeBattleRunner.WIDTH/30 ), (int)(PokeBattleRunner.HEIGHT*7/40));
			g.setColor(status.color);
			g.drawString(status.label, (int)(PokeBattleRunner.WIDTH/35 ), (int)(PokeBattleRunner.HEIGHT*9/40));
		}
	}
	
	
}
