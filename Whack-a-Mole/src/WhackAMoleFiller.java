
// Filler code for Whack a Mole by Mr. Friedman

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

public class WhackAMoleFiller {

	// size of the display area
    private int windowWidth = 800, windowHeight = 600, textHeight = 35; 
    
    // variable for your "mole" image - do you need more?
    private Image moleImg;

    // constants for the number of moles, number of moles appearing at any time, and the 
    // time gap between new moles popping up (in milliseconds)
    private final int NUMMOLES = 10, NUMAPPEARING = 2, TIMEGAP = 2000;
    
    private int score = 0;
    
    
    // more instance variables/arrays...?
    
    
    public void setup() {
    	
    	// loads your 3 images - all you need to do is match file names.
		try {
			moleImg = ImageIO.read(new File("Images/diglett.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// your code here
    }

    public void draw(Graphics g) {
        
        // your code here
    }


    // what you want to happen when the mouse is clicked
    public void click(int mouseX, int mouseY) {
    	
    	// your code here
    }
    
    // what you want to happen when the time for the current round ends
    public void timeAdvance() {
    	
    	// your code here
    }
    
    // reset the game (molehills should relocate)
    public void reset() {
    	
    	// your code here
    }


    // DO NOT TOUCH BELOW CODE //

    public WhackAMoleFiller() {
    	
    	setup();

        JFrame window = new JFrame();
        window.setTitle("Whack A Mole");
        window.setSize(windowWidth, windowHeight + textHeight);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JTextArea scoreDisplay = new JTextArea();
        scoreDisplay.setEditable(false);
        scoreDisplay.setText("\t\tScore: 0");
        
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
                window.getContentPane().repaint();

			}
        });
        
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(windowWidth, textHeight));
        topPanel.add(resetButton);
        scoreDisplay.setBackground(topPanel.getBackground());
        
        topPanel.add(scoreDisplay);

        

        JPanel canvas = new JPanel() {
            public void paint(Graphics g) {
                draw(g);
                scoreDisplay.setText("\t\tScore: " + score);
            }
        };
        canvas.setPreferredSize(new Dimension(windowWidth, windowHeight));

        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                click(e.getX(), e.getY());
                window.getContentPane().repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        window.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                windowWidth = window.getWidth();
                windowHeight = window.getHeight() - textHeight;
                topPanel.setPreferredSize(new Dimension(windowWidth, textHeight));
                canvas.setPreferredSize(new Dimension(windowWidth, windowHeight));
            }
        });

        container.add(topPanel);
        container.add(canvas);
        window.add(container);
        window.setVisible(true);
        canvas.revalidate();
        window.getContentPane().repaint();
        
        new Timer(TIMEGAP, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	timeAdvance();
            	window.getContentPane().repaint();
            }
        }).start();
        

        
        try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        container.repaint();
    }
    

    public static void main(String[] args) {
        new WhackAMoleFiller();
    }

}