import java.awt.Graphics;
import java.util.ArrayList;

public class Pawn extends Piece{
	
	public Pawn(int turn, int v, String piece_name) {
		super(turn,v,piece_name);
		value = 1;
	}

	@Override
	public ArrayList<int[]> get_moves(Board board, int x, int y) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		int i;
		if (team == 0)
			i = 1;
		else
			i = -1;
		
		if (y+i < 8 && y+i >= 0 && board.getBoard()[x][y+i].get_team() == -1) {
			int[] move = {x,y+i};
			moves.add(move);
		}
		if (team == 0 && y == 1 && board.getBoard()[x][y+2].get_team() == -1 && board.getBoard()[x][y+1].get_team() == -1) {
			int[] move = {x, y+2};
			moves.add(move);
		}
		if (team == 1 && y == 6 && board.getBoard()[x][y-2].get_team() == -1 && board.getBoard()[x][y-1].get_team() == -1) {
			int[] move = {x, y-2};
			moves.add(move);
		}
		if (x+1 < 8 && y+i < 8 && y+i >= 0)
			if (board.getBoard()[x + 1][y+i].get_team() == (team+1)%2) {
				int[] move = {x+1,y+i};
				moves.add(move);
			}
		if (x-1 >= 0 && y+i < 8 && y+i >= 0)
			if (board.getBoard()[x -1][y+i].get_team() == (team+1)%2) {
				int[] move = {x-1,y+i};
				moves.add(move);
			}
		
		
		return moves;
	}


	@Override
	public boolean is_empty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean check(int kingx, int kingy, int x, int y, Board board) {
		if (team == 0) {
			if (kingx - x == 1)
				if (Math.abs(kingy - y) == 1)
					return true;
		}
		if (team == 1) {
			if (kingy - x == -1)
				if (Math.abs(kingy - y) == 1)
					return true;
		}
		return false;
	}

}
