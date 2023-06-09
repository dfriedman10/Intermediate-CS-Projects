package Shootas;

public class Uzi extends Gun{
	static int UZIDAMAGE = 5;
	static int UZIAMMO = 50;
	static int UZISPEED = 38;
	//constructor holds info for gun
	public Uzi() {
		super(UZIDAMAGE, UZIAMMO, UZISPEED, "ShootasImages/uzi", 50, 50, "gun");
		// TODO Auto-generated constructor stub
	}
	//reload returns amount of ammo in this gun
	public void reload() {
		ammo = UZIAMMO;
	}
}
