import java.awt.Graphics;
import java.util.ArrayList;

public class Empty extends Piece{
	
	public Empty() {
		super(-1,-2,"Empty");
	}

	@Override
	public ArrayList<int[]> get_moves(Board board, int x, int y) {
		return null;
	}


	@Override
	public boolean is_empty() {
		return true;
	}

	@Override
	public boolean check(int kingx, int kingy, int x, int y, Board board) {
		// TODO Auto-generated method stub
		return false;
	}
}
