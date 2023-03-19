import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

public class Knight extends Piece {

	public Knight(int turn, int v, String piece_name) {
		super(turn,v,piece_name);
		value = 3;
	}
	
	
	@Override
	public ArrayList<int[]> get_moves(Board board, int x, int y) {
		
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		if (x - 2 >= 0) {
			if (y - 1 >= 0 && board.getBoard()[x-2][y-1].get_team()!= team) {
				int[] move = {x-2,y-1};
				moves.add(move);
			}
			if (y + 1 < 8 && board.getBoard()[x-2][y+1].get_team()!= team) {
				int[] move = {x-2,y+1};
				moves.add(move);
			}
		}
		if (x -1 >= 0) {
			if (y - 2 >= 0 && board.getBoard()[x-1][y-2].get_team()!= team) {
				int[] move = {x-1,y-2};
				moves.add(move);
			}
			if (y + 2 < 8 && board.getBoard()[x-1][y+2].get_team()!= team) {
				int[] move = {x-1,y+2};
				moves.add(move);
			}
		}
		if (x +1 < 8) {
			if (y - 2 >= 0 && board.getBoard()[x+1][y-2].get_team()!= team) {
				int[] move = {x+1,y-2};
				moves.add(move);
			}
			if (y + 2 < 8 && board.getBoard()[x+1][y+2].get_team()!= team) {
				int[] move = {x+1,y+2};
				moves.add(move);
			}
		}
		if (x + 2 < 8) {
			if (y - 1 >= 0 && board.getBoard()[x+2][y-1].get_team()!= team) {
				int[] move = {x+2,y-1};
				moves.add(move);
			}
			if (y + 1 < 8 && board.getBoard()[x+2][y+1].get_team()!= team) {
				int[] move = {x+2,y+1};
				moves.add(move);
			}
		}
		return moves;
	}


	
	public boolean is_empty() {
		return false;
	}


	@Override
	public boolean check(int kingx, int kingy, int x, int y, Board board) {
		
		int x_dist = kingx - x;
		int y_dist = kingy - y;
		
		if (Math.abs(x_dist) == 2 && Math.abs(y_dist) == 1)
			return true;
		else if (Math.abs(x_dist) == 1 && Math.abs(y_dist) == 2)
			return true;
		return false;
	}
	
	

}
