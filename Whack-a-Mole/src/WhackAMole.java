
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
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

public class WhackAMole {

    private int windowWidth = 800, windowHeight = 600, textHeight = 35; //Height and Width of the window
    
    private Image moleImg, bgImg, dirtImg;

    private final int NUMMOLES = 10, NUMAPPEARING = 2, TIMESTEP = 1000;
    
    private int[] x = new int[NUMMOLES], y = new int[NUMMOLES];
    private boolean[] showing = new boolean[NUMMOLES];
    
    private int score = 0;
    
    // instance variables...?
    
    private int imgWidth = windowWidth/12, moleHeight = imgWidth *6/5, dirtHeight = imgWidth/2;
    
    
    public void setup() {
   
    	x = new int[NUMMOLES];
    	y = new int[NUMMOLES];
    	showing = new boolean[NUMMOLES];
    	
    	// loads your 3 images - all you need to do is match file names.
		moleImg = Toolkit.getDefaultToolkit().getImage("Images/diglett.png");
		bgImg = Toolkit.getDefaultToolkit().getImage("Images/background.png");
		dirtImg = Toolkit.getDefaultToolkit().getImage("Images/dirt.png");
		
		for (int i = 0; i < x.length; i++) {
			x[i] = (int)(Math.random()*windowWidth*9/10);
			y[i] = (int)(Math.random()*windowHeight/2 + windowHeight/3);
			
		}
		
		chooseMoles();
    }
    
    private void chooseMoles() {
    	showing = new boolean[NUMMOLES];
		int numTrue = 0;
		while (numTrue < NUMAPPEARING) {
			int randIndex = (int)(Math.random()*showing.length);
			if (!showing[randIndex]) {
				numTrue ++;
				showing[randIndex] = true;
			}
		}
    }

    public void draw(Graphics g) {
    	
        g.drawImage(bgImg, 0, 0, windowWidth, windowHeight, null);
        
        for (int i = 0; i < x.length; i++) {
        	if (showing[i])
        		g.drawImage(moleImg, x[i], y[i], imgWidth, moleHeight, null);
        	else
        		g.drawImage(dirtImg, x[i], y[i] + moleHeight/2, imgWidth, dirtHeight, null);
        	
        }
    }

    public void click(int mouseX, int mouseY) {
    	for (int i = 0; i < x.length; i++) {
    		if (!showing[i]) continue;
    		double dist = Math.sqrt(Math.pow(mouseX - (x[i] + imgWidth/2), 2) + Math.pow(mouseY - (y[i] + moleHeight/2), 2));
    		if (dist < imgWidth/2) {
    			score += 1;
    			showing[i] = false;
    		}
    	}
    }
    
    public void timeAdvance() {
    	chooseMoles();
    }
    
    // reset the game
    public void reset() {
    	
    	setup();
    	score = 0;
    }


    // DO NOT TOUCH BELOW CODE //

    public WhackAMole() {
    	
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
                scoreDisplay.setText("\t\tScore: " + score);
                draw(g);
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
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image mouseImage = toolkit.getImage("Images/diglett.png");
        Cursor c = toolkit.createCustomCursor(mouseImage , new Point(0, 
                   0), "img");
        
        
        window.setCursor(c);

        
        window.setVisible(true);
        
        new Timer(TIMESTEP, new ActionListener() {
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
        new WhackAMole();
    }

}