//POKEMON POACH - MADE BY ERIC TORII

//note: text location and size value exempt from magic numbers 
//EXTRA HELP: 
	
	//Mr.Friedman template
	//Mr. Friedman helped multiple times, including fixing minors bugs, and also showing how to do Key Listener and Timer
	//some help from Vinnie, Zander, Nico to take a look at bugs

//KEY CONTROLS: right click with your mouse/mousepad to shoot
//				press R to reload
//				press the reset button at the top or press F to reset the game and score completely

//EXTRA FEATURE: Has a gun shooting the target, and you can RELOAD (r) the gun when it's out of ammo

//imports below
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PokemonHunt {
	
    // constants for the number of moles, number of moles appearing at any time, and the 
    // time gap between new moles popping up (in milliseconds)
	
	//other changeable functions like MAXTIMESECONDS can change time left, score, or max bullets. 
	//Note: If max bullets changed, animation will not display bullets more than ten on screen.
    private final int NUMMOLES = 14, NUMAPPEARING = 7, TIMEGAP = 2500;
    private final int MAXBULLETS = 10; private final int MAXTIMESECONDS = 40;
    private int score = 0; private final int pointsPerHit = 100;

	// size of the display area
    private int windowWidth = 1395, windowHeight = 850, textHeight = 35; 
    int targetSize = 120;
    
    //image variables are below
    private Image backgroundImg;
    private Image bushImg;
    private Image pikachuImg;
    private Image cornerGunImg;
    private Image downsightImg;
    private Image bulletImg;
    private Image menuImg;
    private Image manPointImg;
    private Image reloadingImg;
    
    //other instance variables, and arrays
   	int [] x, y;
   	int cornerGunSize = 450;
   	int bushWidth = 170;
   	int bushHeight = 100;
   	int mousePressX = 0;
   	int mousePressY = 0;
   	int bulletsLeft = 10;
   	int wordGrow = 0;
   	int reloadWordGrow = 0;
   	int timeLeft = MAXTIMESECONDS;
   	int reloadTime = 1000;
   	int bigScopeTime = 20;
   	int inGameTimeOneSecond = 1000;
   	int themeSongTimeInMilliseconds = 243000;
   	int reloadWordChangeSize = 20;
   	int secondSubtracted = 1;
   	int bulletSubtracted = 1;
   	int xLengthSubtractor = 1;
   	
   	//Graphic instance variables 
   	int topCornerX = 0;
   	int topCornerY = 0;
   	int smallScopeSize = 1800;
   	int bigScopeSize = 2500;
   	int numMolesXStartingValue = 5;
   	int numMolesXSpawnRange = 1300;
   	int numMolesYStartingValue = 270;
   	int numMolesYSpawnRange = 400;
   	int pikachuBushAdjustmentSpaceX = 30;
   	int pikachuBushAdjustmentSpaceY = 50;
   	int scoreWordChangeSize = 10;
   	int bulletLength = 205;
   	int bulletWidth = 115;
   	int smallScopeXAdjustment = 1153;
   	int smallScopeYAdjustment = 631;
   	int bigScopeXAdjustment = 1605;
   	int bigScopeYAdjustment = 920;
   	
   	//boolean to turn on and off showing
   	boolean [] showing;
   	//boolean to check for clicks for scopes and other
   	boolean clicked = false;
   	//boolean for reloading functions
   	boolean rPressed = false;
   	//boolean to display menu screen
   	boolean menuOn = true;
   	//boolean for scope animation
    boolean bigScope = true;
    
    //for audio functions
    Clip clip;
    AudioInputStream inputStream;
    
    //Timer for reloading gun, there is a pause
    Timer reloadTimer = new Timer (reloadTime, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	
        	bulletsLeft = MAXBULLETS;
        	rPressed = false;
        	reloadTimer.stop();
        }
    });

    //Timer for pause before displaying larger scope to imitate recoil function
    Timer bigScopeTimer = new Timer(bigScopeTime, new ActionListener() {
	  @Override
	  public void actionPerformed(ActionEvent ae) {
	  	
	  	bigScope = false;
	  	bigScopeTimer.stop();
	  	
	  }
	});
    
    //Timer to tick down 1 second every time 1000 milliseconds pass
    Timer gameTimer = new Timer (inGameTimeOneSecond, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	
        	if (timeLeft > 0) {
        		
        		timeLeft -= secondSubtracted;
        		
        	}
        	
        	gameTimer.stop();
        	
        }
    });
    
    //replays the music when the song ends, so music always plays
    Timer replayThemeMusicTimer = new Timer (themeSongTimeInMilliseconds, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	
        	playTheme();
        	replayThemeMusicTimer.stop();
        	
        }
    });
	
    public void setup() {
    	
    	//code to load images
		backgroundImg = Toolkit.getDefaultToolkit().getImage("pokemon background.jpeg");
		bushImg = Toolkit.getDefaultToolkit().getImage("bush.png");
		pikachuImg = Toolkit.getDefaultToolkit().getImage("pikachu.png");
		cornerGunImg = Toolkit.getDefaultToolkit().getImage("cornergun.png");
		downsightImg = Toolkit.getDefaultToolkit().getImage("downsight.png");
		bulletImg = Toolkit.getDefaultToolkit().getImage("bullet.png");
		menuImg = Toolkit.getDefaultToolkit().getImage("shooting pikachu.jpeg");
		manPointImg = Toolkit.getDefaultToolkit().getImage("manpoint.png");
		reloadingImg = Toolkit.getDefaultToolkit().getImage("john wick reload.jpeg");
		
		
		//array of trues and falses (that will be switched on and off)
		showing = new boolean [NUMMOLES];
	
		//random x of targets spawn location
		x = new int [NUMMOLES];
		
		for (int i = 0; i < x.length; i++) {
			
			x[i] = (int)(Math.random()* numMolesXSpawnRange + numMolesXStartingValue);
			
		}
		
		//random y of targets spawn location
		y = new int [NUMMOLES];
		
		for (int i = 0; i < x.length; i++) {

			y[i] = (int)(Math.random()* numMolesYSpawnRange + numMolesYStartingValue);
			
		}
    }

    public void draw(Graphics g) {
    	
    	//background green grass display
    	g.drawImage(backgroundImg, topCornerX, topCornerY, windowWidth, windowHeight, null);
    	
    	//Pokemon and Bush Graphic spaawn
		for (int i = 0; i < x.length; i ++) {
			
			if (showing [i] == true) {
			
				g.drawImage(pikachuImg, x[i], y[i], targetSize, targetSize, null);
			
			}
			
			g.drawImage(bushImg, x[i] - pikachuBushAdjustmentSpaceX, y[i] + pikachuBushAdjustmentSpaceY, bushWidth, bushHeight, null);
		}
		
		//bullets for bullet counter bar on the side. Each time bullets Left drops, an image disappears
		if (bulletsLeft > 9) {
			g.drawImage(bulletImg, 30, 20, bulletLength, bulletWidth, null);
		}
		if (bulletsLeft > 8) {
			g.drawImage(bulletImg, 30, 85, bulletLength, bulletWidth, null);
		}
		if (bulletsLeft > 7) {
			g.drawImage(bulletImg, 30, 150, bulletLength, bulletWidth, null);
		}
		if (bulletsLeft > 6) {
			g.drawImage(bulletImg, 30, 215, bulletLength, bulletWidth, null);
		}
		if (bulletsLeft > 5) {
			g.drawImage(bulletImg, 30, 280, bulletLength, bulletWidth, null);
		}
		if (bulletsLeft > 4) {
			g.drawImage(bulletImg, 30, 345, bulletLength, bulletWidth, null);
		}
		if (bulletsLeft > 3) {
			g.drawImage(bulletImg, 30, 410, bulletLength, bulletWidth, null);
		}
		if (bulletsLeft > 2) {
			g.drawImage(bulletImg, 30, 475, bulletLength, bulletWidth, null);
		}
		if (bulletsLeft > 1) {
			g.drawImage(bulletImg, 30, 540, bulletLength, bulletWidth, null);
		}
		if (bulletsLeft > 0) {
			g.drawImage(bulletImg, 30, 605, bulletLength, bulletWidth, null);
		} 	
		
		//reload reminder text when out of ammo (will grow upon click)
		if (bulletsLeft == 0 && rPressed == false) {
			
			g.setColor(Color.white);
			g.setFont(new Font("impact", Font.BOLD, 75 + reloadWordGrow));
			g.drawString("RELOAD (R)", 50 - reloadWordGrow/2, 700 + reloadWordGrow/2);
			
		} 
    	
    	//corner gun, which disappears when in scoping mode
    	if (clicked == false && bigScope == false && rPressed == false|| bulletsLeft == 0) {
    		
    		g.drawImage(cornerGunImg, windowWidth - cornerGunSize, windowHeight - cornerGunSize, cornerGunSize, cornerGunSize, null);
    		
    	}
    	
    	//reloading image of John Wick during reload
    	if (rPressed == true) {
    		
    		g.drawImage(reloadingImg, 500, 230, 400, 400, null);
    		g.setColor(Color.white);
			g.setFont(new Font("impact", Font.BOLD, 55));
			g.drawString("RELOADING...", 560, 600);	
 
    	}
    	
		//text score && time
		g.setColor(Color.white);
		g.setFont(new Font("IMPACT", Font.BOLD, 80 + wordGrow));
		g.drawString("SCORE: " + String.valueOf(score), 900 - wordGrow/2, 105 + wordGrow/2);
		
		g.setColor(Color.white);
		g.setFont(new Font("IMPACT", Font.BOLD, 50));
		g.drawString("TIME LEFT: " + String.valueOf(timeLeft), 900, 175);

		//downsight gun, only shows when clicked
		if (clicked == true && rPressed == false) {
			
			g.drawImage(downsightImg, mousePressX - smallScopeXAdjustment, mousePressY - smallScopeYAdjustment, smallScopeSize, smallScopeSize, null);
			
		}
		
		if (bigScope == true && bulletsLeft > 0 && rPressed == false) {
			
			g.drawImage(downsightImg, mousePressX - bigScopeXAdjustment, mousePressY - bigScopeYAdjustment, bigScopeSize, bigScopeSize, null);
			
		}

		//menu Screen
		if (menuOn == true) {
			
		    g.drawImage(menuImg, 0, 0, windowWidth, windowHeight, null);	
		    g.drawImage(manPointImg, 900, 400, 500, 500, null);
	    	
			g.setColor(Color.white);
			g.setFont(new Font("IMPACT", Font.BOLD, 200));
			g.drawString("PIKACHU POACH", 65, 275);
			
			g.setColor(Color.white);
			g.setFont(new Font("Impact", Font.BOLD, 30));
			g.drawString("GAME BY STUDIO TORII", 65, 320);
			
			g.setColor(Color.white);
			g.setFont(new Font("Impact", Font.BOLD, 50));
			g.drawString("CLICK ANYWHERE TO START", 65, 420);
			
		}
	  
		//end screen Graphics when time is up
		if (timeLeft == 0) {

			g.setColor(Color.black);
		    g.fillRect(0, 0, windowWidth, windowHeight);		
		    g.setColor(Color.white);
			g.setFont(new Font("IMPACT", Font.BOLD, 250));
			g.drawString("GAME OVER", 140, 400);
			g.setFont(new Font("IMPACT", Font.BOLD, 80));
			g.drawString("Score: " + score, 160, 550);
			g.setFont(new Font("comfortaa", Font.BOLD, 30));
			g.drawString("Press (F) to try again.", 160, 620);
		    
		}
		
		gameTimer.start();
		
    }


    // what happens when mouse clicked
    public void click(int mouseX, int mouseY) {

    	//if you have no bullets left, and you unclick, the word for the score shrinks
    	//gunshot plays
    	if (bulletsLeft > 0 && timeLeft > 0){
    		
    		wordGrow += scoreWordChangeSize;
    		 playGunshot();
    		 
    	}
    	
    	//no ammo empty gun chamber sound plays
    	if (bulletsLeft == 0 && timeLeft > 0){
    		
    		playNoAmmo();
    		
    	}
    	
    	//if you have no bullets left, and you unclick, the word for reload shrinks
    	if (bulletsLeft == 0) {
    		
    		reloadWordGrow += reloadWordChangeSize;
    		
    	}

    	//if you haven't reloaded and you still have bullets, this happens on click
    	if (bulletsLeft > 0 && rPressed == false) {
    		
    		mousePressX = mouseX;
    		mousePressY = mouseY;
    		clicked = true;	
    		bigScope = false;
    		bulletsLeft -= bulletSubtracted;
    		
    		//REGISTERING A SUCCESSFUL HIT ON A PIKACHU (it disappears and add pts)
			for (int i = 0; i < x.length; i ++) {
				
				if ((showing [i]) && mouseX <= x[i] + targetSize && mouseX >= x[i] && mouseY >= y[i] && mouseY <= y[i] + targetSize) {
					
					showing [i] = false;
					
					if (timeLeft > 0) {
						
						score += pointsPerHit;
						
					}	
					
				}	
			}
    	}
    }
    
    //What happens when you let go of the mouse button
    public void unclick(int mouseX, int mouseY) {
    	
    	clicked = false;
    	
    	//if you have no bullets left, and you unclick, the word for the score shrinks
    	if (bulletsLeft > 0){
    		
    		wordGrow -= scoreWordChangeSize;
    	}
    	
    	//if you have no bullets left, and you unclick, the word for reload shrinks
    	if (bulletsLeft == 0) {
    		
    		reloadWordGrow -= reloadWordChangeSize;
    	}
    	
    	bigScope = true;
    	bigScopeTimer.start();
    	
    }
    
    //what happens when you press keys
    public void keyPress(KeyEvent e) {
		
    	//what happens when you press r
		if (e.getKeyChar() == 'r') {
			
			wordGrow = 0;
			reloadWordGrow = 0;
			rPressed = true;
			reloadTimer.start();
			
			//when you press r, and r has already been pressed, you can't repeat this
			if (rPressed == true) {

				wordGrow = 0;
				
				//on the end screen, you can't reload
				if (timeLeft > 0) {
					
					playReload();
				}
			
			}
		}	
		
		//when you press f, you also reset game
		if (e.getKeyChar() == 'f') {
			
			reset();
			
		}	
	}
  
    // what you want to happen when the time for the current round of pikachus ends
    public void timeAdvance() {
    	
    	//when time ends, make all pikachus disappear briefly
    	for (int i = 0; i < showing.length; i++) {
			
			showing [i] = false;
			
		}
    	
    	int count = 0;
    	
    	//checks for places where pikachus already popped out, and fills up empty slots until NUMAPPEARING
    	while (count < NUMAPPEARING) {
    		
    		int randI = (int)(Math.random()* (x.length - xLengthSubtractor) + 0);
    		
    		//function for the randI, counts until NUMAPPEARING
    		if (showing [randI] == false) {
    			
    			showing[randI] = true;
   			
    			count ++;
    		}
    	}
    }
    
    //reset the game
    public void reset() {

    	// turns showing on for all pikachus
    	for (int i = 0; i < x.length; i++) {
    	    
        	showing [i] = true;
    		
    	}

    	setup();
    	score = 0;
    	wordGrow = 0;
    	bulletsLeft = MAXBULLETS;
    	timeLeft = MAXTIMESECONDS;
    	
    }


    // DO NOT TOUCH BELOW CODE //

    public PokemonHunt() {
    	
    	setup();

    	playTheme();
        JFrame window = new JFrame();
        window.setTitle("Pokemon Hunt");
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
		        window.requestFocus();
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
        
        //allows game to register keys
        window.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {    	
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				keyPress(e);   
                window.getContentPane().repaint();

			}

			@Override
			public void keyReleased(KeyEvent e) { 
				
			}
        	
        });

        //allows game to register mouse actions
        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
            	
            	
        		 click(e.getX(), e.getY());
                 window.getContentPane().repaint();
                 menuOn = false;
            	
            }

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {
            	unclick(e.getX(), e.getY());
                window.getContentPane().repaint();
                
            }

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
        
        //adds timer between mole rounds
        new Timer(TIMEGAP, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	timeAdvance();
            	window.getContentPane().repaint();
            }
        }).start();
        
        
        //THIS IS A KEY FEATURE THAT REPAINTS THE GAME/ controls time.
        //this constantly repaints the whole game every 5 milliseconds, allowing all other timer functions to move
        new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
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
        window.requestFocus();
    }

    //plays Gunshot sound
	public synchronized void playGunshot() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					BufferedInputStream in = new BufferedInputStream(new FileInputStream("Gunshot Sound Effect.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println("hello" + e.getMessage());

				}

			}

		}).start();

	}

	//plays out of ammo sound
	public synchronized void playNoAmmo() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					BufferedInputStream in = new BufferedInputStream(new FileInputStream("no ammo.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println("hello" + e.getMessage());

				}

			}

		}).start();

	}
	
	//plays reload sound
	public synchronized void playReload() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					BufferedInputStream in = new BufferedInputStream(new FileInputStream("reload.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println("hello" + e.getMessage());

				}

			}

		}).start();

	}

	//plays theme song
	public synchronized void playTheme() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					BufferedInputStream in = new BufferedInputStream(new FileInputStream("theme song eric.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println("hello" + e.getMessage());
					
				}
				
				if (timeLeft == 0) {
					return;
				}

			}

		}).start(); 
		
		replayThemeMusicTimer.start();

	}

    public static void main(String[] args) {
       
    	new PokemonHunt();
     
    }

}