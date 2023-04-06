
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class PokeBattleRunner {

	public Image background, pokeballImg;	
	
	public static int WIDTH = 950, HEIGHT = WIDTH*700/1280;
	public static int POKEWIDTH, POKEHEIGHT;
	public static int HEALTHWIDTH, HEALTHHEIGHT;	// all dimensions/locations scaled to match WIDTH/HEIGHT
	public static int ATTACKWIDTH, ATTACKHEIGHT;
	public static int[] POKEX, POKEY;
	
	public Pokemon[] pokes = new Pokemon[2];	// holds the 2 currently battling pokemon
	
	private int turn = (int)(Math.random()*2);	//randomly generates starting turn
	
	public JPanel canvas;		// canvas that displays the battle
	public JTextArea text;		// text box for message displays
	
	private Type attackType = null;		// holds the current attack's type and location for animation
	private int attackX, attackY;
	
	private final double BATTLESPEED = 1;	// normal speed is 1. smaller = faster
	private final double ANIMATIONSPEED = 20;	// speed of attack img's motion. normal is 20
	
	// matrix for calculating type multipliers
	public static final double[][] effectiveness = {{1,1,1,1},{1,.5,.5,2},{1,2,.5,.5},{1,.5,2,.5}};

	
	public PokeBattleRunner() {
		getClasses();
		initGraphics();
		rest(.1);
		battle();
	}
	
	// runs the battle
	public void battle() {

		// shows each pokemon's score, for testing purposes
		System.out.println(pokes[0]);
		System.out.println(pokes[1]);

		
		// display matchup then pause
		text.setText(pokes[0].getName() +" vs "+ pokes[1].getName());
		rest(BATTLESPEED);
		
		// runs battle until one is dead
		while (pokes[0].getHealth()>0 && pokes[1].getHealth()>0) {
			
			text.setText(pokes[turn].getName()+"'s turn!");
			canvas.repaint();
			rest(BATTLESPEED);
			
			// apply current affect to current pokemon
			// and display the outcome (if any) 
			String message = pokes[turn].effects();
			text.setText(message);
			if (message.length() != 0) {
				canvas.repaint();
				rest(BATTLESPEED);
			}
			
			// generates current pokemon's random move (null if pokemon can't move)
			Move m = pokes[turn].attack();
			if (m != null) {
				
				text.setText(pokes[turn].getName() + " used " + m.name+".\n");
				
				attackType = m.type;
				animateAttack();
				
				// applies damage/effect to opponent and displays outcome
				message = pokes[(turn+1)%2].suffer(m);
				text.setText(text.getText()+message);

				canvas.repaint();
				rest(BATTLESPEED);
			}
			
			// move to next pokemon
			turn = (turn+1)%2;
		}
		
		// determines the dead pokemon and display
		int fainted = pokes[0].getHealth()==0 ? 0:1;
		text.setText(pokes[fainted].getName() +" fainted.");
		canvas.repaint();
	}
	
	// draws the pokemon and attack img
	public void drawBattle(Graphics g) { 
		for (int i=0; i<pokes.length; i++) pokes[i].draw(g,i==1); 
		
		if (attackType!= null) {
			g.drawImage(attackType.img, attackX, attackY, ATTACKWIDTH,ATTACKHEIGHT,null);
		}
	}
	
	// moves the attack img across the screen
	public void animateAttack() {
		attackX = POKEX[turn]+POKEWIDTH/2;
		attackY = POKEY[turn]+POKEHEIGHT/4;
		while (turn==0 ? attackX < POKEX[1] : attackX > POKEX[0]+POKEWIDTH/2) {
			attackX += (POKEX[turn]-POKEX[(turn+1)%2]+(turn==0?-1:1)*POKEWIDTH/2)/-ANIMATIONSPEED;
			attackY += (POKEY[turn]-POKEY[(turn+1)%2])/-ANIMATIONSPEED;
			canvas.repaint();
			rest(BATTLESPEED/ANIMATIONSPEED);
		}
		attackType = null;
	}
	
	// pauses for a specified time
	public void rest(double time) {
		try {
			Thread.sleep((int)(time*1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// uses prompts to load Pokemon subclasses
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getClasses() {
		try {
			String p1 = JOptionPane.showInputDialog("Enter the first Pokemon Name");
			String p2 = JOptionPane.showInputDialog("Enter the second Pokemon Name");
			
			Class cls = Class.forName(p1);
			pokes[0] = (Pokemon) cls.getDeclaredConstructor().newInstance();
			cls = Class.forName(p2);
			pokes[1] = (Pokemon) cls.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			System.out.println("Pokemon does not exist..");
			System.exit(1);
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// sets up the GUI 
	@SuppressWarnings("serial")
	public void initGraphics() {
		
		text = new JTextArea();
		text.setEditable(false);
		text.setText("hello everyone");
		
		// generates dimensions/locations for all objects based on current window size
		updateSizes();
		
		background = new ImageIcon(getClass().getClassLoader().getResource("resources/pokeBG.png")).getImage();
		pokeballImg = new ImageIcon(getClass().getClassLoader().getResource("resources/pokeball.png")).getImage();
		
		JFrame frame = new JFrame("Pokemon Battle");
		frame.setSize(WIDTH, HEIGHT);
		
		// area that will display the battle
		canvas = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(background, 0, 0, PokeBattleRunner.WIDTH, PokeBattleRunner.HEIGHT, null);
				super.paintComponents(g);
				drawBattle(g);
			}
		};
		canvas.setLayout(null);
		canvas.add(text);
		frame.add(canvas);
		
		// updates dimensions/locations when the window is resized
		canvas.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				WIDTH = canvas.getWidth();
				HEIGHT = canvas.getHeight();
				updateSizes();
				canvas.repaint();
			}
		});
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setVisible(true);
	}
	
	// uses window dimensions to determine all other dimensions/locations
	public void updateSizes() {
		POKEWIDTH = WIDTH*4/15;
		POKEHEIGHT = HEIGHT*10/24;
		HEALTHWIDTH = WIDTH/5;
		HEALTHHEIGHT = HEIGHT/45;
		ATTACKWIDTH = POKEWIDTH/4;
		ATTACKHEIGHT = POKEHEIGHT/4;
		POKEX = new int[]{WIDTH/10,WIDTH*6/10}; POKEY = new int[]{HEIGHT*8/24,HEIGHT*3/40};
		text.setFont(new Font("Courier",Font.PLAIN, PokeBattleRunner.HEIGHT/20));
		text.setBounds(WIDTH/20,HEIGHT*78/100,WIDTH*17/20,HEIGHT/5);
	}
	
	public static void main(String[] args) {
		new PokeBattleRunner();
	}
}
