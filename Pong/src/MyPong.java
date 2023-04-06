

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

public class MyPong extends JPanel implements KeyListener, Runnable {

	// constants that are predefined and won't change
	private final int width = 600, height = 600, window_height = 650;
	private final int paddle_width = 20, radius = 8, paddle_height = 100;
	private final int ball_speed = 2, paddle_speed = 4;
	private final int goal = 3;
	private final int laser_height = 10,laser_width = 30, laser_speed = 6;

	// booleans to keep track of the direction of our paddles
	private boolean up1, down1, up2, down2;
	
	// keeps track of the velocity of our ball
	private int ball_vx, ball_vy;

	private ArrayList<Rectangle> lasers = new ArrayList<Rectangle>();
	
	// records the position of the ball and paddles
	private int ballx, bally, p1y, p1x, p2y, p2x;

	// we call our items Rectangles to use the built-in functions
	private Rectangle ball, player1, player2;

	// variables to keep track of the game
	private boolean pause, game_over;
	private int score1 = 0, score2 = 0;
	private String status;
	
	private boolean solo = false;
	
	
// michael furlissi

	public void move_ball() {

		// move the ball according to its current velocity
		ball.x += ball_vx;
		ball.y += ball_vy;
		
		for (Rectangle laser : lasers) {
			if (laser.width == laser_width)
				laser.x += laser_speed;
			else
				laser.x -= laser_speed;
		}
	}
	
	public void move_paddles() {
		// uses our paddle direction booleans to move our paddles accordingly
		if (up1) {
			player1.y -= paddle_speed;
			down1 = false;
		}
		else if (down1) {
			player1.y += paddle_speed;
			up1 = false;
		}
		if (up2 && !solo) {
			player2.y -= paddle_speed;
			down2 = false;
		}
		else if (down2 && !solo) {
			player2.y += paddle_speed;
			up2 = false;
		}
		else if (solo && ball.y > player2.y) {
			player2.y += paddle_speed;
		}
		else if (solo && ball.y < player2.y) {
			player2.y -= paddle_speed;
		}
	}
	
	public void check_collisions() {
		// bounces the ball off a paddle if there is a collision
		if (ball.intersects(player1)) {
			if (ball.x < player1.x-radius)
				ball.x -= 30;
			else
				ball_vx *=-1;
		}
		
		else if (ball.intersects(player2)) {
			if (ball.x > player2.x)
				ball.x += 30;
			else
				ball_vx *=-1;
		}	
	
		// bounces the ball off the top/bottom walls 
		if (ball.y <= 0 || ball.y + ball.height >= height) {
			ball_vy *= -1;
		}

		// if the ball reaches a left/right wall, records the score and resets 
		// the game
		if (ball.x <= 0) {
			ball_vx = 0;
			ball_vy = 0;
			score2 ++;
			initializeVariables();
		}
		if (ball.x + ball.width >= width) {
			ball_vx = 0;
			ball_vy = 0;
			score1 ++;
			initializeVariables();
		}
	}
	
	public void check_scores() {
		if (score1 >= goal) {
			game_over = true;
			status = "Player 1 wins!\nPress restart to play again";
		}
		if (score2 >= goal) {
			game_over = true;
			status = "Player 2 wins!\nPress restart to play again";
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
				
				move_ball();
				move_paddles();
				check_collisions();
			}
			
			check_scores();
			
			//rests for a hundredth of a second
			try {
				Thread.sleep(10);
			} catch (Exception ex) {}

		}
	}
	
	// very simple main method to get the game going
	public static void main(String[] args) {
		MyPong game = new MyPong(); 
		game.start_game();
	}
 
	// does complicated stuff to initialize the graphics and key listeners
	public void start_game() {
		
		initializeVariables();
		JFrame frame = new JFrame();
		JButton button = new JButton("restart");
		frame.setSize(width, window_height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(this);
		frame.add(button, BorderLayout.SOUTH);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restart();
				MyPong.this.requestFocus();
			}
		});
		
		this.addKeyListener(this);
		this.setFocusable(true);
		Thread t = new Thread(this);
		t.start();
	}

	// defines what we want to happen anytime we draw the game
	public void paint(Graphics g) {
		ArrayList<Integer> remove_list = new ArrayList<Integer>();
		int i = 0;
		for (Rectangle laser : lasers) {
			
			if (laser.intersects(player1)) {
				remove_list.add(i);
				player1.height -= 10;
			}
			else if (laser.intersects(player2)) {
				remove_list.add(i);
				player2.height -= 10;
			}
			else 
				i++;
		}
		for (int j : remove_list) {
			lasers.remove(j);
		}
		
		// background color is gray
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, width, height);
		
		// draws the ball blue
		g.setColor(Color.blue);
		g.fillOval(ball.x, ball.y, ball.width, ball.height);
		
		// draws the paddles green
		g.setColor(Color.green);
		g.fill3DRect(player1.x, player1.y, player1.width, player1.height, true);
		g.fill3DRect(player2.x, player2.y, player2.width, player2.height, true);
		
		// writes the score of the game
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.setColor(Color.red);
		g.drawString("P1 Score: " + score1, width/5, 20);
		g.drawString("P2 Score: " + score2, width*3/5, 20);
		
		g.setColor(Color.RED);
		for (Rectangle laser : lasers) 
			g.fill3DRect(laser.x, laser.y, laser_width, laser_height, true);
		
		
		// if the game is paused, displays a message saying so
		if (pause || game_over) 
			g.drawString(status, width/3, height/3);
			
			
	}

	@Override
	// checks if a keyboard button has been pressed
	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		
		// changes paddle direction based on what button is pressed
		if (keyCode == KeyEvent.VK_DOWN) 
			down2 = true;
		
		if (keyCode == KeyEvent.VK_UP) 
			up2 = true;

		if (e.getKeyChar() == 'w')
			up1 = true;
		
		if (e.getKeyChar() =='s')
			down1 = true;
		
		if (e.getKeyChar() == ' ') {
			pause = !pause;
			status = "Paused.\nPush space to play";
		}
		if (e.getKeyChar() == '1')
			solo = true;
		else if (e.getKeyChar() == '2')
			solo = false;
	}

	@Override
	// checks if a keyboard button has been released
	public void keyReleased(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		
		// stops paddle motion based on which button is released
		if (keyCode == KeyEvent.VK_UP) 
			up2 = false; 

		if (keyCode == KeyEvent.VK_DOWN) 
			down2 = false;
  
		if(e.getKeyChar() == 'w')
			up1 = false;
		
		if (e.getKeyChar() == 's')
			down1 = false;
		
		if (keyCode == KeyEvent.VK_LEFT) 
			lasers.add(new Rectangle(p2x - 5 - laser_width, player2.y + player2.height/2, laser_width-1, laser_height));
		
		
		if (e.getKeyChar() == 'd')
			lasers.add(new Rectangle(p1x + paddle_width + 5, player1.y + player1.height/2, laser_width, laser_height));

	}
	
	@Override
	// method does nothing
	public void keyTyped(KeyEvent e) {}

	// restarts the game, including scores
	public void restart() {
		initializeVariables();
		score1 = 0;
		score2 = 0;
	}

	// returns the paddles and ball to their original positions. does not reset score
	public void initializeVariables(){
	 
		up1 = false;
		down1 = false;
		up2 = false;
		down2 = false;
		
		ballx = height/2;
		bally = height/2;
		
		p1x = 0;
		p1y = height/2;
			
		p2x = width-paddle_width;
		p2y = height/2;

		ball = new Rectangle(ballx,bally, radius,radius);
		player1 = new Rectangle(p1x,p1y,paddle_width,paddle_height);
		player2 = new Rectangle(p2x,p2y,paddle_width,paddle_height);

		ball_vx = ball_speed;
		ball_vy = ball_speed;
		pause = true;
		status = "Paused.\nPush space to play";
		game_over = false;
		lasers = new ArrayList<Rectangle>();

     
	}
}