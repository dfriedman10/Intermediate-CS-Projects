
import java.awt.Color;

public class SuperBot extends Bot{

	public SuperBot(MazeRunner mr, Color c) {
		super(mr, c);
		// TODO Auto-generated constructor stub
	}
		int lefturncount =1;
		int threeturn =0;
		int turncount =0;
		int frontwall=0;
	@Override
	public void move() {
		if(lefturncount%2 !=0)
		{
			turnLeft();
			lefturncount++;
//			System.out.println("1");
		}
		else if(threeturn==1)
		{
			turnLeft();
			turncount++;
//			System.out.println("tc "+ turncount);
			if(turncount ==3)
			{
				turncount=0;
				threeturn=0;
				frontwall=1;
			}
//			System.out.println("2" + " "+threeturn);
		}
		else if(moveForward()==true)
		{
			lefturncount++;
//			System.out.println("3");
		}
		else
		{
			threeturn =1;
			
//			System.out.println("4");
		}
//		System.out.println();
	}}// 