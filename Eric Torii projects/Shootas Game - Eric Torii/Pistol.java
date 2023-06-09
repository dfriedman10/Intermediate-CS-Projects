package Shootas;

public class Pistol extends Gun {
	static int PISTOLDAMAGE = 20;
	static int PISTOLAMMO = 2;
	static int PISTOLSPEED = 50;
	//constructor holds info for gun
	public Pistol() {
		super(PISTOLDAMAGE, PISTOLAMMO, PISTOLSPEED, "ShootasImages/pistol", 40, 35, "gun");
		// TODO Auto-generated constructor stub
	}
	//reload returns amount of ammo in this gun
	public void reload() {
		ammo = PISTOLAMMO;
	}

}
