package Shootas;
//images not displaying
//bullet number not switching over

//SHOOTAS (Game by Eric Torii)
//note * please download the Shootas Game, ShootasImages, and ShootasMusic
//SEE READ ME FILE

//glitches:
//sniper accidentally does 20 damage at times
//bullets disappear when off the screen

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import spaceInvaders.Laser;
import spaceInvaders.SpaceThing;

public class ShootasGame {
	//image variables are below, for the backgrounds and menu
	Image background;
	Image menuScreen;
	Image menuScreen2;
	Image menuScreen3;
	Image background1pic;
	Image background2pic;
	Image background3pic;
	Image background4pic;
	Image background5pic;
	//values for height and width of the screen
	int WIDTH = 1400, HEIGHT = 870;
	int fontSize = 40;
	int bullet1Width = 20;
	int bullet1Height = 20;
	int topFontSize = 50;
	int menuValue = 0;
	int mapSelectValue = 0;
	//BOOLEANS for background and menu -- each time you click, you toggle through these
	boolean menuOn = true;  
	boolean menu2On = true;
	boolean menu3On = true;
	//width and height of the player
	private int PLAYERWIDTH = 70, PLAYERHEIGHT = (5*PLAYERWIDTH/4); 
	// our list of list of lasers for both players
	private ArrayList<ShootasLaser> p1Lasers = new ArrayList<ShootasLaser>();
	private ArrayList<ShootasLaser> p2Lasers = new ArrayList<ShootasLaser>();
	//Adding the elements of the fame
	private PlayerThing player1;
	private PlayerThing player2;
	private Platform mainPlatform; 
	private Platform leftPlatform; 
	private Platform rightPlatform; 
	private Box mysteryBox; 
	//point the players start at
	int player1Xstart = WIDTH/7;
	int player1Ystart = HEIGHT/3 - PLAYERHEIGHT/2;
	int player2Xstart = 6*WIDTH/7-PLAYERWIDTH;
	int player2Ystart = 5*HEIGHT/7 - PLAYERHEIGHT/2;
	int PLAYERSPEED = 7;
	final int FINALPLAYERSPEED = PLAYERSPEED;
	//pistol laser: shoots slow speed, each bullet moves fast. Long, one sec time before reload sound
	//sniper laser: shoots slow speed, bullet moves super fast. ONE SHOT KILL
	//uzi lasers: fast speed, bullets fast, bullets do low damage but high ammo count
	//AR laser: medium speed, bullets medium (faster than pistol), medium damage
	
	//keeps tracks of current ammo count
	int p1gunAmmo = 2;
	int p2gunAmmo = 2;
	//time for special gun to be held
	int specialGunTimeMilliseconds = 4000;
	//time between mystery box spawnings
	int mysteryBoxSpawnTimeMilliseconds = 6000;
	//amount of variability for box spawn time
	int timeRandomRange = 4000;
	//WARNING: mysteryBoxSpawnTimeMilliseconds should always be GREATER than specialGunTimeMilliseconds
		//it's important that the player cannot hold the gun long enough to pick up a new box
		
	//changing variable that moves the walls
	int wallMoveSpeed = 1;
	int secondsBeforeWallMove = 250;
	int mainPlatformX = (WIDTH/2)-30; 
	int mainPlatformY = 0; 
	int boxX = (WIDTH/2)-30; 
	int boxY = HEIGHT/2; 
	int boxSideLength = 100;
	int p1reloadTime = 1200;
	int p2reloadTime = 1200;
	//clock of game
	int inGameTimeOneSecond = 1000;
	int timepassed = 0;
	//lives of players
	final int startinglives = 300;
	int player1lives = startinglives, player2lives = startinglives;
	// booleans to keep track of the game's progress
	boolean gameover = false;
	boolean paused = false;
	boolean p1reloadPressed = false;
	boolean p2reloadPressed = false;
	boolean player1Ready = false;
	boolean player2Ready = false;
	//SPECIAL EFFECTS AND OTHER
	int adjustmentX = fontSize/2;
	int livesFontSize = 50;
	int p1fontGrow = 0;
	int p2fontGrow = 0;
	int fontGrowSize = 20;
	int growTimeMilliseconds = 200;
	int player1CharacterChooseValue = 0;
	int player2CharacterChooseValue = 0;
	//BELOW ARE TIMERS
    //Timer for reloading gun, there is a pause before you get all ammo back
    Timer p1reloadTimer = new Timer (p1reloadTime, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	if (p1gunAmmo <= 0) {
        		player1.gun.reload();
			}
        	p1gunAmmo = player1.gun.ammo;
        	p1reloadTimer.stop();
        }
    });
    //below is the reload timer for player 2 (see above)
    Timer p2reloadTimer = new Timer (p2reloadTime, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	if (p2gunAmmo <= 0) {
            	player2.gun.reload();
			}
        	p2gunAmmo = player2.gun.ammo;
        	p2reloadTimer.stop();
        }
    });
    //shrink back down the words that grow (like "lives" grows on impact)
    Timer p1WordShrink = new Timer (growTimeMilliseconds, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	p1fontGrow -= fontGrowSize;
        	p1WordShrink.stop();
        }
    });
    //shrinks for player 2 (see above)
    Timer p2WordShrink = new Timer (growTimeMilliseconds, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	p2fontGrow -= fontGrowSize;
        	p2WordShrink.stop();
        }
    });
    //this is moving the left platform inwards
    Timer moveLeftPlatform = new Timer (secondsBeforeWallMove, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	leftPlatform.moveX(wallMoveSpeed);
        }
    });
    //this is moving the right platform inwards
    Timer moveRightPlatform = new Timer (secondsBeforeWallMove, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	rightPlatform.moveX(-wallMoveSpeed);
        }
    });
    //BOX TIMER, spawns during set time, spawns
    Timer mysteryBoxTimer = new Timer (mysteryBoxSpawnTimeMilliseconds + (int)(Math.random()*timeRandomRange), new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	if (gameover != true && paused != true) {
        		mysteryBox = new Box(WIDTH/2 - boxSideLength/2, (int)(Math.random()*(HEIGHT-(3*boxSideLength/2))), boxSideLength, boxSideLength); 
        	}
        	mysteryBoxTimer.stop();
        }
    }); 
    
    //timer for special weapon
    Timer p1BackToPistol = new Timer (specialGunTimeMilliseconds, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	player1.gun = new Pistol();
        	p1gunAmmo = player1.gun.ammo;
        	p1BackToPistol.stop();
        }
    });
    Timer p2BackToPistol = new Timer (specialGunTimeMilliseconds, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
        	player2.gun = new Pistol();
        	p2gunAmmo = player2.gun.ammo;
        	p2BackToPistol.stop();
        }
    });
	// movement of players and lasers
	public void move() {
		if (mysteryBox == null && menu3On == false) {
			mysteryBoxTimer.start();
		}
		//the player 1 pistol bullet
		for(int i = 0; i < p1Lasers.size(); i++) {	
			p1Lasers.get(i).moveX(player1.gun.speed);
			//this removes a player laser from the list when it reaches the bottom of the list
			if (p1Lasers.get(i).x >= WIDTH || p1Lasers.get(i).x <= 0) {
				p1Lasers.remove(0);
			}
		}
		//the the player 2 pistol bullet
		for(int i = 0; i < p2Lasers.size(); i++) {	
			p2Lasers.get(i).moveX(-player2.gun.speed);
			//this removes a player laser from the list when it reaches the bottom of the list
			if (p2Lasers.get(i).x >= WIDTH || p2Lasers.get(i).x <= 0) {
				p2Lasers.remove(0);
			}
		}
		//This moves player 1
		player1.move();
		//This moves player 2
		player2.move();
		//moves the platforms at the pace of the timers
		moveLeftPlatform.start();
		moveRightPlatform.start();
		if (leftPlatform.x + leftPlatform.width >= (WIDTH/2 - WIDTH/8)) {
			moveLeftPlatform.stop();
		}
		if (rightPlatform.x <= (WIDTH/2 + WIDTH/8)) {
			moveRightPlatform.stop();
		}
		//player 1 cannot go beyond left wall
		if (player1.x <= 0) {
			player1.speedx = 0; 
			player1.x += 2; 
		}
		//player 1 cannot go beyond ceiling
		if (player1.y <= 0) {
			player1.speedy = 0; 
			player1.y += 2; 
		}
		//player 1 cannot go beyond bottom
		if (player1.y + 4*PLAYERHEIGHT/3 >= HEIGHT) {
			player1.speedy = 0; 
			player1.y -= 2; 
		}
		//player 2 cannot go beyond right wall
		if (player2.x + PLAYERWIDTH >= WIDTH) {
			player2.speedx = 0; 
			player2.x -= 2; 
		}
		//player 2 cannot go beyond ceiling
		if (player2.y <= 0) {
			player2.speedy = 0; 
			player2.y += 2; 
		}
		//player 2 cannot go beyond bottom
		if (player2.y + 4*PLAYERHEIGHT/3 >= HEIGHT) {
			player2.speedy = 0; 
			player2.y -= 2; 
		}
	}
	//stat change for guns when different ones are picked up
	
	//collision between players and bullets
	//collision with platform
	public void checkCollisions() {
		//picking up weapon
		if (mysteryBox != null) {
			//riley coded this as a possible debug
			double poop = Math.random();
			//what happens when an intersection happens between p1 and mysteryBox
			if (player1.intersects(mysteryBox)) {
				if (player1.gun.getClass() != Pistol.class) {
					p1BackToPistol.start();
				}
				playPickup();
				//runs gun Stats here because only on collision do you need to check the stats
				if (poop >= 0 && poop < 0.33) {
					player1.gun = new Sniper();
				}
			 	if (poop >= 0.33 && poop < 0.66) {
			 		player1.gun = new AR();
				}
			 	if (poop >= 0.66 && poop <= 1) {
			 		player1.gun = new Uzi();
				}
			 	p1gunAmmo = player1.gun.ammo;
				mysteryBox = null;
				return;
			}
			if (player2.intersects(mysteryBox)) {
				if (player2.gun.getClass() != Pistol.class) {
					p1BackToPistol.start();
				}
				playPickup();
				//runs gun Stats here because only on collision do you need to check the stats
				//debug
				if (poop >= 0 && poop < 0.33) {
					player2.gun = new Sniper();
				}
			 	if (poop >= 0.33 && poop < 0.66) {
			 		player2.gun = new AR();
				}
			 	if (poop >= 0.66 && poop <= 1) {
			 		player2.gun = new Uzi();
				}
			 	p2gunAmmo = player2.gun.ammo;
				mysteryBox = null;
			}
		}
		//collision for pistol bullets - p1 bullet and p2
		for (int i = 0; i < p1Lasers.size(); i++) {
			if (player2.intersects(p1Lasers.get(i))) {
				p1Lasers.remove(i);
				player2lives -= player1.gun.damage;
				playhit();
				p2fontGrow = fontGrowSize;
				p2WordShrink.start();
				//nerf Sniper so you can only hit once
				if (player1.gun.getClass() == Sniper.class) {
					player1.gun = new Pistol();
				}
			}
		}
		//collision for pistol bullets - p2 bullet and p1
		for (int i = 0; i < p2Lasers.size(); i++) {
			if (player1.intersects(p2Lasers.get(i))) {
				p2Lasers.remove(i);
				player1lives -= player2.gun.damage;
				playhit();
				p1fontGrow = fontGrowSize;
				p1WordShrink.start();
				//nerf Sniper so you can only hit once
				if (player2.gun.getClass() == Sniper.class) {
					player2.gun = new Pistol();
				}
			}
		}
		//player1 cannot move beyond platform
		if (player1.intersects(mainPlatform)) {
			player1.speedx = 0; 
			player1.x -= 2; 
		}
		//player2 cannot move beyond platform
		if (player2.intersects(mainPlatform)) {
			player2.speedx = 0; 
			player2.x += 2; 
		}
		//player 1 cannot move beyond the left platform
		if (player1.intersects(leftPlatform)) {
			player1.speedx = 0; 
			player1.x += 2; 
		}
		//player 2 cannot move beyond the right platform
		if (player2.intersects(rightPlatform)) {
			player2.speedx = 0; 
			player2.x -= 2; 
		}
	}
	//resetting the game back to Menu 3 when the game is over
	public void reset() {
		setup();
		selectionSound();
		//rebuild lasers
		p1Lasers = new ArrayList<ShootasLaser>();
    	p2Lasers = new ArrayList<ShootasLaser>(); 
	  	player1lives = startinglives;
	  	player2lives = startinglives; 
	  	timepassed = 0;
	  	gameover = false;
	  	menu3On = true;
	  	player1Ready = false;
		player2Ready = false;
		//nerf Sniper so you can only hit once
		player1.gun = new Pistol();
		player2.gun = new Pistol();
	}
	// set up your variables, lists, etc here
	public void setup() {
		//SCROLLING THROUGH LIBRARY OF CHARACTERS FOR P1
		if (player1CharacterChooseValue == 0) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Iron Man", "gun", true); 
		}
		if (player1CharacterChooseValue == 1) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Asian Princess", "gun", true); 
		}
		if (player1CharacterChooseValue == 2) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Dummy", "gun", true); 
		}
		if (player1CharacterChooseValue == 3) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Football Man", "gun", true); 
		}
		if (player1CharacterChooseValue == 4) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Grandpa", "gun", true); 
		}
		if (player1CharacterChooseValue == 5) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Jack", "gun", true); 
		}
		if (player1CharacterChooseValue == 6) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Jenny", "gun", true); 
		}
		if (player1CharacterChooseValue == 7) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Jimmy", "gun", true); 
		}
		if (player1CharacterChooseValue == 8) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Kratos", "gun", true); 
		}
		if (player1CharacterChooseValue == 9) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Timmy", "gun", true); 
		}
		if (player1CharacterChooseValue == 10) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/White Princess", "gun", true); 
		}
		if (player1CharacterChooseValue == 11) {
			player1 = new PlayerThing(player1Xstart, player1Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Zombie", "gun", true); 
		}
		//SCROLLING THROUGH LIBRARY OF CHARACTERS FOR P1
		if (player2CharacterChooseValue == 0) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Iron Man", "gun", false); 
		}
		if (player2CharacterChooseValue == 1) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Asian Princess", "gun", false); 
		}
		if (player2CharacterChooseValue == 2) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Dummy", "gun", false); 
		}
		if (player2CharacterChooseValue == 3) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Football Man", "gun", false); 
		}
		if (player2CharacterChooseValue == 4) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Grandpa", "gun", false); 
		}
		if (player2CharacterChooseValue == 5) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Jack", "gun", false); 
		}
		if (player2CharacterChooseValue == 6) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Jenny", "gun", false); 
		}
		if (player2CharacterChooseValue == 7) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Jimmy", "gun", false); 
		}
		if (player2CharacterChooseValue == 8) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Kratos", "gun", false); 
		}
		if (player2CharacterChooseValue == 9) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Timmy", "gun", false); 
		}
		if (player2CharacterChooseValue == 10) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/White Princess", "gun", false); 
		}
		if (player2CharacterChooseValue == 11) {
			player2 = new PlayerThing(player2Xstart, player2Ystart, PLAYERWIDTH, PLAYERHEIGHT , "ShootasImages/Zombie", "gun", false); 
		}
		//adding in the middle, left, right platforms
		mainPlatform = new Platform(mainPlatformX, mainPlatformY, WIDTH - 2 * mainPlatformX, HEIGHT); 
		leftPlatform = new Platform(-WIDTH, 0, WIDTH, HEIGHT); 
	 	rightPlatform = new Platform(WIDTH - WIDTH/500, 0, WIDTH, HEIGHT); 
	 	mysteryBox = null;
		//for menu image
		try {
			menuScreen = ImageIO.read(new File("ShootasImages/menu.png"));
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		//for 2nd menu image
		try {
			menuScreen2 = ImageIO.read(new File("ShootasImages/menu2.png"));
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		//for 3rd menu image
		try {
			menuScreen3 = ImageIO.read(new File("ShootasImages/menu3.png"));
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		//for background pictures of game (can scroll through)
		try {
			background1pic = ImageIO.read(new File("ShootasImages/background1.png"));
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		try {
			background2pic = ImageIO.read(new File("ShootasImages/background2.png"));
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		try {
			background3pic = ImageIO.read(new File("ShootasImages/background3.png"));
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
		try {
			background4pic = ImageIO.read(new File("ShootasImages/background4.png"));
		} catch (IOException e) {
			System.out.println("Image file not found");
		}
	}
	//shooting guns player 1
	public void fireP1Laser() {
		//adds the lasers
		p1Lasers.add(new ShootasLaser(player1.x + PLAYERWIDTH, player1.y + 3*PLAYERHEIGHT/5, bullet1Width, bullet1Height, "ShootasImages/bluebullet"));
		for (int i = 0; i < p1Lasers.size(); i++) {
			//if passes the sides of the walls, delete laser
			if (p1Lasers.get(i).x >= WIDTH || p1Lasers.get(i).x <= 0) {
				p1Lasers.remove(0);
			}
		}
	}
	//shooting guns player 2
	public void fireP2Laser() {
		//adds the lasers
		p2Lasers.add(new ShootasLaser(player2.x - PLAYERWIDTH/3, player2.y + 3*PLAYERHEIGHT/5, bullet1Width, bullet1Height, "ShootasImages/redbullet"));
		for (int i = 0; i < p2Lasers.size(); i++) {
			//if passes the sides of the walls, delete laser
			if (p2Lasers.get(i).x >= WIDTH || p2Lasers.get(i).x <= 0) {
				p2Lasers.remove(0);
			}
		}
	}
	//draws all graphics
	public void draw(Graphics g) {
		//black background screen
	   	g.setColor(Color.black);
	   	g.fillRect(0, 0, WIDTH, HEIGHT);
	   	g.setFont(new Font("impact", Font.BOLD, topFontSize));
	   	//if a certain background is selected, display
	   	if (mapSelectValue == 0) {
			g.drawImage(background1pic, 0,0, WIDTH,HEIGHT,null);
		}
		if (mapSelectValue == 1) {
			g.drawImage(background2pic, 0,0, WIDTH,HEIGHT,null);
		}
		if (mapSelectValue == 2) {
			g.drawImage(background3pic, 0,0, WIDTH,HEIGHT,null);
		}
		if (mapSelectValue == 3) {
			g.drawImage(background4pic, 0,0, WIDTH,HEIGHT,null);
		}
		if (mapSelectValue == 4) {
			g.setColor(Color.white);
		   	g.fillRect(0, 0, WIDTH, HEIGHT);
		}
	   	//draws the platforms
	   	rightPlatform.draw(g);
		leftPlatform.draw(g);
		mainPlatform.draw(g);
		//draws lives at the top for p1
		g.setColor(Color.BLUE);
		g.drawString(" P1", WIDTH/25, HEIGHT/10);
		g.drawString("        LIVES:               AMMO: " + p1gunAmmo, WIDTH/25, HEIGHT/10);
		g.setFont(new Font("impact", Font.BOLD, livesFontSize + p1fontGrow));
		g.drawString(""+player1lives, 9*WIDTH/50, HEIGHT/10);
		//draws lives for p2
		g.setFont(new Font("impact", Font.BOLD, topFontSize));
		g.setColor(Color.RED);
		g.drawString(" P2", WIDTH/2 + WIDTH/10, HEIGHT/10);
		g.drawString("        LIVES:               AMMO: " + p2gunAmmo, WIDTH/2 + WIDTH/10, HEIGHT/10);
		g.setFont(new Font("impact", Font.BOLD, livesFontSize + p2fontGrow));
		g.drawString(""+player2lives, 37*WIDTH/50, HEIGHT/10);
		//if menu 3 is on, have background selector
		if (menu3On == true) {
			g.setColor(Color.black);
			g.drawImage(menuScreen3, 0,0, WIDTH, HEIGHT,null);
			if (mapSelectValue == 0) {
				g.drawImage(background1pic, 630,600, WIDTH/4,HEIGHT/4,null);
			}
			if (mapSelectValue == 1) {
				g.drawImage(background2pic, 630,600, WIDTH/4,HEIGHT/4,null);
			}
			if (mapSelectValue == 2) {
				g.drawImage(background3pic, 630,600, WIDTH/4,HEIGHT/4,null);
			}
			if (mapSelectValue == 3) {
				g.setColor(Color.black);
				g.drawImage(background4pic, 630,600, WIDTH/4,HEIGHT/4,null);
			}
			if (mapSelectValue == 4) {
				g.setColor(Color.white);
			   	g.fillRect(630, 600, WIDTH/4, HEIGHT/4);
			}
		}
		if (mysteryBox != null)
			mysteryBox.draw(g);
		//draws player characters
	  	player1.draw(g);
	  	player2.draw(g);
	  	//reloading graphic for p1
	  	if (p1reloadPressed == true) {
    		g.setColor(Color.blue);
			g.setFont(new Font("impact", Font.BOLD, 30));
			g.drawString("RELOADING...", player1.x - PLAYERWIDTH/3, player1.y - PLAYERHEIGHT/5);	
			if (p1gunAmmo > 0) {
				p1reloadPressed = false;
			}
    	}
	  	//reloading graphic for p2
	  	if (p2reloadPressed == true) {
    		g.setColor(Color.red);
			g.setFont(new Font("impact", Font.BOLD, 30));
			g.drawString("RELOADING...", player2.x - PLAYERWIDTH/3, player2.y - PLAYERHEIGHT/5);	
			if (p2gunAmmo > 0) {
				p2reloadPressed = false;
			}
    	}
	  	//draws bullets
	  	for (ShootasLaser laser: p1Lasers) {
    		laser.draw(g);
    	}
	  	for (ShootasLaser laser: p2Lasers) {
    		laser.draw(g);
    	}
		//pause screen
		if (this.paused && gameover != true) {
			g.setColor(Color.blue);
			g.setFont(new Font("impact", Font.BOLD, 300));
			g.drawString("PAUSED", WIDTH/4 - WIDTH/15, 3*HEIGHT/5);
		}
		//if menu2 is on, draw this
		if (menu2On == true) {
		g.drawImage(menuScreen2, 0,0, WIDTH,HEIGHT,null);
		}
	   	//if menu is on, draw this
		if (menuOn == true) {
		   	g.drawImage(menuScreen, 0,0, WIDTH,HEIGHT,null);
		}
		//ready up screen for player 1
		if (menu3On == true && player1Ready) {
			g.setColor(Color.black);
		   	g.fillRect(0, 0, WIDTH/2, HEIGHT);
			g.setColor(Color.cyan);
			g.setFont(new Font("impact", Font.BOLD, 200));
			g.drawString("READY", 120, 4*HEIGHT/7);
		}	
		//ready up screen for player 2
		if (menu3On == true && player2Ready) {
			g.setColor(Color.black);
		   	g.fillRect(WIDTH/2, 0, WIDTH/2, HEIGHT);
			g.setColor(Color.pink);
			g.setFont(new Font("impact", Font.BOLD, 200));
			g.drawString("READY", 760, 4*HEIGHT/7);
		}
		//START button to battle
		if (menu3On == true && player2Ready && player1Ready) {
			g.setColor(Color.white);
			g.setFont(new Font("impact", Font.BOLD, 50));
			g.drawString("(press space)", 540, 5*HEIGHT/7);
		}
		//gameover if one person dies
		if (player1lives <= 0 || player2lives <= 0) {
			gameover = true;
		}
		//game over screen
		if (gameover == true){
			//blue gameover text if p1 wins
			if (player1lives > player2lives) {
				g.setColor(Color.blue);
			}
			//red gameover text if p2 wins
			if (player1lives < player2lives) {
				g.setColor(Color.red);
			}
			g.setFont(new Font("impact", Font.BOLD, 200));
			g.drawString("GAMEOVER!", WIDTH/4 - WIDTH/20, 3*HEIGHT/5);
			g.setFont(new Font("impact", Font.BOLD, 50));
			if (player1lives > player2lives) {
				g.drawString("Player 1 Wins        (press R to play again)", WIDTH/4 - WIDTH/35, 2*HEIGHT/5);
			}
			if (player1lives < player2lives) {
				g.drawString("Player 2 Wins         (press R to play again)", WIDTH/4 - WIDTH/35, 2*HEIGHT/5);
			}
			if (player1lives == player2lives) {
				g.drawString("TIE", WIDTH/4 - WIDTH/35, 2*HEIGHT/5);
			}
		}
	}
	
	// ******* DON'T TOUCH BELOW CODE (lol I did, a lot.)************//  
	public ShootasGame() {
		//runs setup
		setup();
		JFrame frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel canvas = new JPanel() {
			public void paint(Graphics g) {
				draw(g);
			}
		};
		//main game control
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, false), "Pause");
		canvas.getActionMap().put("Pause", new PauseAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, false), "Reset");
		canvas.getActionMap().put("Reset", new ResetAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), " ");
		canvas.getActionMap().put(" ", new StartAction());
		//player 1 controls
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "Left");
		canvas.getActionMap().put("Left", new LeftAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "LeftRelease");
		canvas.getActionMap().put("LeftRelease", new LeftReleaseAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "Right");
		canvas.getActionMap().put("Right", new RightAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "RightRelease");
		canvas.getActionMap().put("RightRelease", new RightReleaseAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "Up");
		canvas.getActionMap().put("Up", new UpAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "UpRelease");
		canvas.getActionMap().put("UpRelease", new UpReleaseAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "Down");
		canvas.getActionMap().put("Down", new DownAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "DownRelease");
		canvas.getActionMap().put("DownRelease", new DownReleaseAction());
		
		//fire P1 laser
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0, false), "1");
		canvas.getActionMap().put("1", new fireP1Action());
		//reload P1 laser
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0, false), "2");
		canvas.getActionMap().put("2", new p1ReloadAction());
		
		//player 2 controls
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "A");
		canvas.getActionMap().put("A", new AAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "ARelease");
		canvas.getActionMap().put("ARelease", new AReleaseAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "D");
		canvas.getActionMap().put("D", new DAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "DRelease");
		canvas.getActionMap().put("DRelease", new DReleaseAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "W");
		canvas.getActionMap().put("W", new WAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "WRelease");
		canvas.getActionMap().put("WRelease", new WReleaseAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "S");
		canvas.getActionMap().put("S", new SAction());
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "SRelease");
		canvas.getActionMap().put("SRelease", new SReleaseAction());
		
		//fires P2 laser
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0, false), "K");
		canvas.getActionMap().put("K", new fireP2Action());
		//reload P2 laser
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0, false), "O");
		canvas.getActionMap().put("O", new p2ReloadAction());
		
		//choose p1 character
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0, false), "F");
		canvas.getActionMap().put("F", new p1SelectAction());
		//choose p2 character
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, false), "H");
		canvas.getActionMap().put("H", new p2SelectAction());
		
		//p1 READY UP
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0, true), "G");
		canvas.getActionMap().put("G", new p1ReadyUpSelectAction());
		//p2 READY UP
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, true), "J");
		canvas.getActionMap().put("J", new p2ReadyUpSelectAction());
		
		//map select
		canvas.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0, true), "M");
		canvas.getActionMap().put("M", new mapSelectAction());

		frame.add(canvas);
		frame.setVisible(true);
		//game words when menu is not on, game is not paused, game is not over, etc.
		while (true) {
			if (!paused && !gameover && player1lives > 0 && menuOn == false && menu2On == false && menu3On == false) {
				move();
				checkCollisions();
				//will return to pistol after timer ends
				if (player1.gun.getClass() != Pistol.class) {
					p1BackToPistol.start();
				}
				//will return to pistol after timer ends
				if (player2.gun.getClass() != Pistol.class) {
					p2BackToPistol.start();
				}
			}
			frame.getContentPane().repaint();
			try {Thread.sleep(20);} 
			catch (InterruptedException e) {}
		}
	}
	//below are all the actions that are binded to KeyStrokes, each one connects to a button you press
	//these are player 1 keys
	private class RightAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			player2.speedx = PLAYERSPEED;
		}
	}
	private class LeftAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			player2.speedx = -PLAYERSPEED;
		}
	}
	private class LeftReleaseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(player2.speedx < 0) {
				player2.speedx = 0;
			}
		}
	}
	private class RightReleaseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(player2.speedx > 0) {
				player2.speedx = 0;
			}
		}
	}
	private class UpAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			player2.speedy = -PLAYERSPEED;
		}
	}
	private class DownAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			player2.speedy = PLAYERSPEED;
		}
	}
	private class UpReleaseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(player2.speedy < 0) {
				player2.speedy = 0;
			}
		}
	}
	private class DownReleaseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(player2.speedy > 0) {
				player2.speedy = 0;
			}
		}
	}
	//player 2 keys and movement
	private class AAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			player1.speedx = -PLAYERSPEED;
		}
	}
	private class DAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			player1.speedx = PLAYERSPEED;
		}
	}
	private class DReleaseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(player1.speedx > 0) {
				player1.speedx = 0;
			}
		}
	}
	private class AReleaseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(player1.speedx < 0) {
				player1.speedx = 0;
			}
		}
	}
	private class WAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			player1.speedy = -PLAYERSPEED;
		}
	}
	private class SAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			player1.speedy = PLAYERSPEED;
		}
	}
	private class SReleaseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(player1.speedy > 0) {
				player1.speedy = 0;
			}
		}
	}
	private class WReleaseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(player1.speedy < 0) {
				player1.speedy = 0;
			}
		}
	}
	//Game control keys
	//menu screen is turned off, game starts
	private class StartAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			//right before the 3rd click, before turning off selection, both players need to ready up first
			if (menuValue < 2) {
				menuValue += 1;
				selectionSound();
			}		
			if (player1Ready && player2Ready && menuValue == 2) {
				//when leaving the MenuScreen3, play these sounds
				menuValue += 1;
				Theme ();
				go();
				go();
			}
			//this scrolls through the menus and turns off the menus
			//menu 1 2 and 3 are all stacked ontop of eachother, just toggleable images
			if (menuValue == 1) {
				menuOn = false;
			}
			if (menuValue == 2) {
				menuOn = false;
				menu2On = false;
			}
			//both people need to ready up for the game to start
			if (menuValue == 3 && player1Ready && player2Ready) {
				menuOn = false;
				menu2On = false;
				menu3On = false;
			}		
			if (menuValue >= 4) {
				menuValue = 2;
			}
		}
	}
	//scrolling through maps button
		private class mapSelectAction extends AbstractAction {
			public void actionPerformed(ActionEvent e) {
				if (menu3On == true && player1Ready == false && player2Ready == false) {
					mapSelectValue = (mapSelectValue+1) % 5; 
					selectionSound();
				}
			}
		}
	//game is paused, all functions except music stops
	private class PauseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if (gameover != true && menuOn == false && menu2On == false && menu3On == false) {
				paused = !paused;
			}
		}
	}
	//scrolls through list of characters for p1
	private class p1SelectAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if (menuOn == false && menu2On == false && menu3On == true && player1Ready == false) {
				player1CharacterChooseValue += 1;
				selectionSound();
				if (player1CharacterChooseValue >= 12) {
					player1CharacterChooseValue = 0;
				}
				setup();
			}
		}
	}
	//scrolls through list of characters for p2
	private class p2SelectAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if (menuOn == false && menu2On == false && menu3On == true && player2Ready == false) {
				player2CharacterChooseValue += 1;
				selectionSound();
				if (player2CharacterChooseValue >= 12) {
					player2CharacterChooseValue = 0;
				}
				setup();
			}
		}
	}
	
	//player 1 shooting
	private class fireP1Action extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if (!paused && !gameover && player1lives > 0 && menuOn == false && menu2On == false  && menu3On == false) {
				if (p1gunAmmo > 0) {
					fireP1Laser();
					p1gunAmmo -= 1;
					if (player1.gun.getClass() == Pistol.class) {
						playDeagle();
					}
					if (player1.gun.getClass() == Uzi.class) {
						playUzi();
					}
					if (player1.gun.getClass() == AR.class) {
						playAR();
					}
					if (player1.gun.getClass() == Sniper.class) {
						playSniper();
					}
				}
				else {
					playNoAmmo();
				}
			}
		}
	}
	//player 2 shooting
	private class fireP2Action extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if (!paused && !gameover && player1lives > 0 && menuOn == false && menu2On == false  && menu3On == false) {
				if (p2gunAmmo > 0){
					fireP2Laser();
					p2gunAmmo -= 1;
					if (player2.gun.getClass() == Pistol.class) {
						playDeagle();
					}
					if (player2.gun.getClass() == Uzi.class) {
						playUzi();
					}
					if (player2.gun.getClass() == AR.class) {
						playAR();
					}
					if (player2.gun.getClass() == Sniper.class) {
						playSniper();
					}
				}
				else {
					playNoAmmo();
				}
			}
			
		}
	}
	
	//player 1 and 2 reloading
	private class p1ReloadAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if (p1gunAmmo <= 0 && !gameover &&!paused && p1reloadPressed == false) {
				p1reloadTimer.start();
				playReload();
				p1reloadPressed = true;
			}
		}
	}
	
	private class p2ReloadAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if (p2gunAmmo <= 0 && !gameover &&!paused && p2reloadPressed == false) {
				p2reloadTimer.start();
				playReload();
				p2reloadPressed = true;
			}
		}
	}
	
	//player 1 and 2 readying up (so both players are ready before the game starts)
	private class p1ReadyUpSelectAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(menuOn == false && menu2On == false && menu3On == true && player1Ready == false) {
				player1Ready = true;
				Ready();
			}
		}
	}
	
	private class p2ReadyUpSelectAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if(menuOn == false && menu2On == false && menu3On == true && player2Ready == false) {
				player2Ready = true;
				Ready();
			}
		}
	}
	
	//activates reset method, reset will set everything back to before the game started
	private class ResetAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			//only can be clicked after you win
			if (gameover == true || paused == true && menuOn == false && menu2On == false && menu3On == false) {
				reset();
				paused = false;
			}
		}
	}
	
	//plays theme song of game
	public synchronized void Theme() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/highest.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}

			}

		}).start();
		
	}
	//play laser sound
	public synchronized void playhit() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/bowhit.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}
			}

		}).start(); 

	}
	
	//pickup item sound
		public synchronized void playPickup() {

			new Thread(new Runnable() {

				public void run() {

					try {

						Clip clip = AudioSystem.getClip();

						//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
						BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/pickup.wav"));

						AudioInputStream inputStream = AudioSystem.getAudioInputStream(

							in);

						clip.open(inputStream);

						clip.start();

					} catch (Exception e) {

						System.err.println(e.getMessage());
						
					}
				}

			}).start(); 

		}
	//play reload sound
	public synchronized void playReload() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/reload.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}
			}

		}).start(); 

	}
	
//	//deagle sound effect
	public synchronized void playDeagle() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/deagleloud.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}
			}

		}).start(); 

	}
	//uzi sound effect
	public synchronized void playUzi() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/uzi.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}
			}

		}).start(); 

	}
	//play AR sound effect
	public synchronized void playAR() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/AR.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}
			}

		}).start(); 

	}
	//play Sniper sound effect
	public synchronized void playSniper() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/sniper.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}
			}

		}).start(); 

	}
	//plays no ammo sound effect when clip empty
	public synchronized void playNoAmmo() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/no ammo2.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}
			}

		}).start(); 

	}
	//play sound effect when game starts "go!"
	public synchronized void go() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/go.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}
			}

		}).start(); 

	}
	//play the sound of selecting an item on the menu
	public synchronized void selectionSound() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/selectsound.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}
			}

		}).start(); 

	}
	//play the ready sound when your game is readying up
	public synchronized void Ready() {

		new Thread(new Runnable() {

			public void run() {

				try {

					Clip clip = AudioSystem.getClip();

					//BufferedInputStream in = new BufferedInputStream(new FileInputStream("spaceInvaders/Image/music.wav"));
					BufferedInputStream in = new BufferedInputStream(new FileInputStream("ShootasMusic/Ready.wav"));

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(

						in);

					clip.open(inputStream);

					clip.start();

				} catch (Exception e) {

					System.err.println(e.getMessage());
					
				}
			}

		}).start(); 

	}
	//run Shootas
	public static void main(String[] args) throws ConcurrentModificationException{
		new ShootasGame();
	}
}
