	import java.awt.BorderLayout;
	import java.awt.Color;
	import java.awt.Font;
	import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.KeyEvent;
	import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
	import javax.swing.JFrame;
	import javax.swing.JPanel;

	public class ChessSaif extends JPanel implements MouseListener, Runnable {
		
		// constants that are predefined and won't change
		private final int width = 600, height = 600;
		private final int square_width = width/8;
		private final int img_width = square_width - 10;
		private final int img_height = square_width - 10;
		private Board board;
		private Piece last_clicked = null;
		private int lastx = 0,lasty = 0;
		private HashMap<String,Image> images;
		private int turn = 0;
		private boolean game_over = false, moved = false;
		
		private boolean human1 = true;
		private boolean human2 = true;
		

		
		public void computer_move() {
			// this is where you want to work.
		}
		
		
		// No need to change below code

		public ChessSaif() {
			board = new Board();
			
			images = new HashMap<String, Image>();
			
			for (int i = 0; i < 8; i ++) {
				Image img = getToolkit().getImage("Images/" + board.getBoard()[i][0].get_name() + ".png");	
				images.put(board.getBoard()[i][0].get_name(), img.getScaledInstance(img_width,img_height, Image.SCALE_SMOOTH));
			}
			for (int i = 0; i < 8; i ++) {
				Image img = getToolkit().getImage("Images/" + board.getBoard()[i][7].get_name() + ".png");	
				images.put(board.getBoard()[i][7].get_name(), img.getScaledInstance(img_width,img_height, Image.SCALE_SMOOTH));
			}
			
			for (int i = 0; i < 8; i ++) {
				Image img = getToolkit().getImage("Images/BlackPawn.png");	
				images.put(board.getBoard()[i][6].get_name(), img.getScaledInstance(img_width,img_height, Image.SCALE_SMOOTH));
			}
			for (int i = 0; i < 8; i ++) {
				Image img = getToolkit().getImage("Images/WhitePawn.png");	
				images.put(board.getBoard()[i][1].get_name(), img.getScaledInstance(img_width,img_height, Image.SCALE_SMOOTH));
			}
		}
		
		public void run() {}

		// very simple main method to get the game going
		public static void main(String[] args) {
			ChessSaif game = new ChessSaif(); 
			game.start_game();
		}
	 
		// does complicated stuff to initialize the graphics and key listeners
		public void start_game() {
			JFrame frame = new JFrame();
			frame.setSize(width+2, height+24);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			frame.add(this);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.setVisible(true);
			this.addMouseListener(this);
			this.setFocusable(true);
			Thread t = new Thread(this);
			t.start();
		}

		// defines what we want to happen anytime we draw the game.
		public void paint(Graphics g) {

			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);

			for (int i = 0; i < 8; i ++) {
				for (int j = 0; j < 8; j ++) {
					g.draw3DRect(i*square_width, j*square_width, square_width, square_width, false);
					/*if ((i+j)%2 == 0) {
						g.setColor(Color.BLACK);
						g.fillRect(i*square_width, j*square_width, square_width, square_width);
						g.setColor(Color.white);
					}*/
					if (!(board.getBoard()[i][j].get_name().equals("Empty"))) {
						if (board.getBoard()[i][j].equals(last_clicked)) {
							g.setColor(Color.yellow);
							g.fillRect(j*square_width +1, i*square_width+1, square_width-1, square_width-1);
						}
						g.drawImage(images.get(board.getBoard()[i][j].get_name()), j*square_width+5, i*square_width+5, this);
					}
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		
			if (!game_over && ((turn == 0 && human1) || (turn == 1 && human2))) {
				Piece clicked = board.getBoard()[e.getY()/square_width][e.getX()/square_width];
				
				if (clicked.get_team() == turn) {
					lastx = e.getX()/square_width;
					lasty = e.getY()/square_width;
					last_clicked = clicked;
				}
				
				else if (last_clicked != null)		{				
					/*for (int[] m:last_clicked.get_moves(board, e.getY()/square_width, e.getX()/square_width)){
						for (int i : m)
							System.out.print(i + " ");
						System.out.println();
					}*/

					if (is_in(e.getY()/square_width, e.getX()/square_width, last_clicked.get_moves(board, lasty, lastx))) {
						if (clicked.value == -1 && clicked.team == 0) {
							System.out.println("Player 2 wins!");
							game_over = true;
						}
						if (clicked.value == -1 && clicked.team == 1) {
							System.out.println("Player 1 wins!");
							game_over = true;
						}
							
						board.move(lasty, lastx, e.getY()/square_width, e.getX()/square_width);
						last_clicked = null;
						if (game_over) {
							repaint();
							return;
						}
						board.check();
						//System.out.println(score(board,turn));
						turn = (turn + 1)%2;
						moved = true;
					}
				}
			}
			if (!game_over&& ((!human1 && turn == 0) || (!human2 && turn == 1))) {
				computer_move();
				board.check();
			}
			if (board.checkMate()) {
				System.out.println("GAME OVER");
				game_over = true;
			}
			repaint();
		}
		
		public boolean is_in(int x, int y, ArrayList<int[]> moves) {
			for (int[] pair : moves) {
				if (x == pair[0] && y == pair[1])
					return true;
			}
			return false;
			
			
		}
		
		public boolean check_board() {
		
			return board.check();
		}
		
		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

	}


