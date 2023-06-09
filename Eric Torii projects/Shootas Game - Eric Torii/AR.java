package Shootas;

public class AR extends Gun{
	static int ARDAMAGE = 10;
	static int ARAMMO = 5;
	static int ARSPEED = 50;
	//constructor holds info for gun
	public AR() {
		super(ARDAMAGE, ARAMMO, ARSPEED, "ShootasImages/AR", 60, 30, "gun");
		// TODO Auto-generated constructor stub
	}
	//reload returns amount of ammo in this gun
	public void reload() {
		ammo = ARAMMO;
	}
}
