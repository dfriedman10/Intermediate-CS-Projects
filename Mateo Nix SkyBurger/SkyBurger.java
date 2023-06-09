package skyBurger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class SkyBurger extends JPanel implements KeyListener, Runnable {

	// constants that are predefined and won't change
	static final int WIDTH = 600, HEIGHT = 600;
	private final int burger_width = 70, burger_height = 40;
	private final int topping_speed = 10, burger_speed = 15, FONTSIZE = 30;

	//the list of images on the burger
	ArrayList<Image> burgerImgList = new ArrayList<Image>();
	// determines the difficulty. The closer to 1.0, the easier the game 
	private double DIFFICULTY = .80;

	private ArrayList<Topping> toppings = new ArrayList<Topping>();
	//amount of money made on the burger
	private double score = 0;
	// records the position of the burger
	private int  burgerY, burgerX;
	//booleans for directions
	private boolean left, right;
	// variables to keep track of the game
	private boolean pause, game_over;
	//message for pauses
	private String status;
	//toppings
	private Topping pickle, cheese, lettuce, patty, tomato, bun;
	//burger and hitbox for the top topping for stacking
	private Burger burger, top_topping;
	//images for backgrounds
	private Image titlescreen, background1, background2, controlling;
	//when the title screen and control image will show up
	private boolean starting = true, controls = true;
	//x and y variables so that I am able to shift backgrounds with the burger
	int imageX = 0;
	static int imageY = 0;
	
	
	

	public void setup() {
		//loading images
		try {
			titlescreen = ImageIO.read(new File("SkyBurgerImages/background.jpg")).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		try {
			background1 = ImageIO.read(new File("SkyBurgerImages/gamebackground1.png")).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		try {
			background2 = ImageIO.read(new File("SkyBurgerImages/gamebackground2.jpg")).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		try {
			controlling = ImageIO.read(new File("SkyBurgerImages/Controls.png")).getScaledInstance(WIDTH, HEIGHT - HEIGHT/10, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		//setting variables to what they need to be when the game starts
		imageY = 0;
		left = false;
		right = false;
		burgerX = WIDTH/2 - burger_width/2;
		burgerY = HEIGHT - HEIGHT/5;
		pause = true;
		status = "Paused.\nPush space to play";
		game_over = false;
		//initializing the burger
		burger = new Burger (burgerX, burgerY, burger_width, burger_height);
		//initializes the burger hitbox using variable in the Burger class that makes the burger get taller
		top_topping = new Burger (burgerX, burgerY-burger.p, burger_width, burger_height/2);
		
		
	}

	public void fall() {
		//setting the values for the toppings
		pickle = new Topping ((int)(Math.random()*(WIDTH - burger_width)), -2*burger_height, burger_width, burger_height, "SkyBurgerImages/pickles.png", "pickle");
		cheese = new Topping ((int)(Math.random()*(WIDTH - burger_width)), -2*burger_height, burger_width, burger_height, "SkyBurgerImages/cheese.png", "cheese");
		lettuce = new Topping ((int)(Math.random()*(WIDTH - burger_width)), -2*burger_height, burger_width, burger_height, "SkyBurgerImages/lettuce.png", "lettuce");
		patty = new Topping ((int)(Math.random()*(WIDTH - burger_width)), -2*burger_height, burger_width, burger_height, "SkyBurgerImages/patty.png", "patty");
		tomato = new Topping ((int)(Math.random()*(WIDTH - burger_width)), -2*burger_height, burger_width, burger_height, "SkyBurgerImages/tomato.png", "tomato");
		bun = new Topping ((int)(Math.random()*(WIDTH - burger_width)), -2*burger_height, burger_width, burger_height, "SkyBurgerImages/topBurger.png", "bun");
		
		//for every frame there is a chance a topping will be added depending on the difficulty
		if (Math.random() > DIFFICULTY) {
			//generates a random number which correlates to a topping
			int rand = (int)(Math.random()*6);
			if (rand ==0) {
				toppings.add(pickle);
			}
			else if (rand ==1) {
				toppings.add(cheese);
			}
			else if (rand ==2) {
				toppings.add(lettuce);
			}
			else if (rand ==3) {
				toppings.add(patty);
			}
			else if (rand ==4) {
				toppings.add(tomato);
			}
			else {
				toppings.add(bun);
			}
		}
		// moving the toppings and removing them once they go off screen
		for (int i = 0; i < toppings.size(); i++) {
			toppings.get(i).y += topping_speed;
			if (toppings.get(i).y> HEIGHT) {
				toppings.remove(i);
			}
		}

	}
	
	public void move_burger() {
		//sets the values so that the burger moves the drawing
		//burger x is what I change for left and right so I am setting burger's x equal to that changing value
		burger.x = burgerX;
		//the burger's y value is often changes in the burger class 
		//so I am setting the sky burger version of the variable equal to that changing value
		burgerY = burger.y;
		//same thing as the burger for these
		top_topping.x = burgerX;
		top_topping.y = burgerY + burger.toppingheight- burger.p ;
		//when an action triggers left boolean to be true the burger moves left
		if (left) {
			burgerX -= burger_speed;
			
		}
		//when an actions trigger right to be true the burger moves right
		if (right) {
			burgerX += burger_speed;
			
		}
	}

	
	@SuppressWarnings("unlikely-arg-type")
	public void check_collisions() {
		//goes through potential toppings
		for (int i = 0; i< toppings.size(); i++) {
			//sees if that topping intersecting with the top item on the burger if it does and it is a bun
			//then the game will end as the burger is commplete
			if (top_topping.intersects(toppings.get(i))&& toppings.get(i).type.endsWith("bun")){
				burger.addTopping(toppings.get(i));
				toppings.remove(i);
				game_over = true;
			}
			//just checks if the topping interesects with the top topping and adds it to the burger
			else if (top_topping.intersects(toppings.get(i))) {
				score +=1.75;
				burger.addTopping(toppings.get(i));
				toppings.remove(i);
			}		
			else {
				
			}
		}
		
	}
	
	
	// runs the actual game once graphics are set up
	public void run() {
		// while(true) should rarely be used to avoid infinite loops, but here it is ok because
		// closing the graphics window will end the program
		while (true) {
	
			// draws the game
			repaint();
			
			// only change things if the game isn't paused
			if (!pause && !game_over) {
				
				fall();
				move_burger();
				check_collisions();
			}
			
			
			//rests for a hundredth of a second
			try {
				Thread.sleep(10);
			} catch (Exception ex) {}

		}
	}
	// defines what we want to happen anytime we draw the game
	public void paint(Graphics g) {
		//control image
		if (controls == true) {
			g.drawImage(controlling, 0, 0, null);
		}
		//starting screen
		if (starting ==true) {
			Font f = new Font("Arial", Font.BOLD, (int)(FONTSIZE *3.0/2.0));
			g.setFont(f);
			g.setColor(Color.GRAY);
			g.drawImage(titlescreen,  0,  0,  null);
			g.drawString("Press Space", WIDTH/20, HEIGHT - HEIGHT/4);
			g.drawString("To Continue", WIDTH/20, HEIGHT - HEIGHT/4 +2*FONTSIZE);
			
		}
		//gameplay screen
		if (starting == false && controls == false) {
			//draw background images basic background and the ones the burger will shift into
			g.drawImage(background1, imageX, imageY, null);
			g.drawImage(background2, imageX, imageY-HEIGHT, null);
			g.drawImage(background2, imageX, imageY-2*HEIGHT, null);
			//changes the location of one of the cloud images so it is seemingly infinite
			if (imageY-HEIGHT >=HEIGHT) {
				imageY = HEIGHT;
			}
			
			g.setColor(Color.ORANGE);
			Font f = new Font("Arial", Font.BOLD, FONTSIZE);
			g.setFont(f);
			// draws toppings falling
			for(int i =0; i<toppings.size(); i++) {
				toppings.get(i).draw(g);
			}
			//draws burgers
			burger.draw(g);
			
			String msg = "Price: "  + score;
			int scoreLength = 4;
			//draws score up top
			g.drawString(msg, WIDTH/2-FONTSIZE/4 *(msg.length()+scoreLength), HEIGHT/10);
			
			// if the game is paused, displays a message saying so
			g.setColor(Color.BLACK);
			if (pause) {
				//displays pause message
				g.drawString(status, WIDTH/5, HEIGHT/3);	
			}
			if (game_over) {
				g.drawString("You earned " + score + "$", WIDTH/4 + FONTSIZE, HEIGHT/2);
				g.drawString("Press Restart", WIDTH/3, HEIGHT/2 +FONTSIZE*2);
			}
		}
			
	}

	
	// very simple main method to get the game going
	public static void main(String[] args) {
		SkyBurger game = new SkyBurger(); 
		game.start_game();
	}
 
	// Mr. Friedman stuff
	public void start_game() {
		setup();
		JFrame frame = new JFrame();
		JButton button = new JButton("restart");
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(this);
		frame.add(button, BorderLayout.SOUTH);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restart();
				SkyBurger.this.requestFocus();
			}
		});
		
		this.addKeyListener(this);
		this.setFocusable(true);
		Thread t = new Thread(this);
		t.start();
	}


	@Override
	// checks if a keyboard button has been pressed
	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		
		// changes burger direction based on what button is pressed
		if (keyCode == KeyEvent.VK_RIGHT) {
			right = true;
		}
		if (keyCode == KeyEvent.VK_LEFT) {
			left = true;
		}
		//continues through slides before the game starts and pauses when is has
		if (e.getKeyChar() == ' ') {
			if(starting == true) {
				starting = false;
			}
			else if (controls == true) {
				controls = false;
			}
			else {
				pause = !pause;
				status = "Paused.\nPush space to play";
			}	
		}
	}
	@Override
	// checks if a keyboard button has been released
	public void keyReleased(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		
		// stops burger motion based on which button is released
		if (keyCode == KeyEvent.VK_RIGHT) 
			right = false; 

		if (keyCode == KeyEvent.VK_LEFT) 
			left = false;
  

	}
	
	@Override
	// method does nothing
	public void keyTyped(KeyEvent e) {}

	// restarts the game
	public void restart() {
		setup();
		toppings.clear();
		score = 0;
	}

	
}