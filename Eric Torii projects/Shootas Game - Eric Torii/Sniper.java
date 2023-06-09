package Shootas;

public class Sniper  extends Gun{
	static int SNIPERDAMAGE = 100;
	static int SNIPERAMMO = 1;
	static int SNIPERSPEED = 60;
	//constructor holds info for gun
	public Sniper() {
		super(SNIPERDAMAGE, SNIPERAMMO, SNIPERSPEED, "ShootasImages/sniper", 80, 30, "gun");
		// TODO Auto-generated constructor stub
	}
	//reload returns amount of ammo in this gun
	public void reload() {
		ammo = SNIPERAMMO;
	}
}
