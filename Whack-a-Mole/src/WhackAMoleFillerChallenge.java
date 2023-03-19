
// Filler code for Whack a Mole by Mr. Friedman

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class WhackAMoleFillerChallenge {

	// size of the display area
    private int windowWidth = 800, windowHeight = 600, textHeight = 35; 
    
    // more instance variables...?
   

    public void draw(Graphics g) {
        
        // your code here
    }


    // what you want to happen when the mouse is clicked
    public void click(int mouseX, int mouseY) {
    	
    	// your code here
    }
    
    // reset the game
    public void reset() {
    	
    	// your code here
    }


    // MAYBE TOUCH THE BELOW CODE? //

    public WhackAMoleFillerChallenge() {

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
            }
        };
        canvas.setPreferredSize(new Dimension(windowWidth, windowHeight));

        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                click(e.getX(), e.getY());
                scoreDisplay.setText("\t\tScore: ");
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
        

        
        try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        container.repaint();
    }
    

    public static void main(String[] args) {
        new WhackAMoleFillerChallenge();
    }

}