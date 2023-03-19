import java.awt.Graphics;
import java.util.ArrayList;

public class Queen extends Piece {
	
	public Queen(int turn, int v, String piece_name) {
		super(turn,v,piece_name);
		value = 9;
	}

	@Override
	public ArrayList<int[]> get_moves(Board board, int x, int y) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		for (int dist = 1; dist < 8; dist ++) {
			if (x + dist > 7 || y + dist > 7)
				break;
			if (board.getBoard()[x + dist][y + dist].get_team() == team)
				break;
			int[] move = {x + dist, y + dist};
			moves.add(move);
			if (board.getBoard()[x + dist][y + dist].get_team() == (team+1)%2)
				break;
		}
		for (int dist = 1; dist < 8; dist ++) {
			if (x - dist < 0 || y + dist > 7)
				break;
			if (board.getBoard()[x - dist][y + dist].get_team() == team)
				break;
			int[] move = {x - dist, y + dist};
			moves.add(move);
			if (board.getBoard()[x - dist][y + dist].get_team() == (team+1)%2)
				break;
		}
		for (int dist = 1; dist < 8; dist ++) {
			if (x - dist < 0 || y - dist < 0)
				break;
			if (board.getBoard()[x - dist][y - dist].get_team() == team)
				break;
			int[] move = {x - dist, y - dist};
			moves.add(move);
			if (board.getBoard()[x - dist][y - dist].get_team() == (team+1)%2)
				break;
		}
		for (int dist = 1; dist < 8; dist ++) {
			if (x + dist > 7 || y - dist < 0)
				break;
			if (board.getBoard()[x + dist][y - dist].get_team() == team)
				break;
			int[] move = {x + dist, y - dist};
			moves.add(move);
			if (board.getBoard()[x + dist][y - dist].get_team() == (team+1)%2)
				break;
		}
		for (int dist = 1; dist < 8; dist ++) {
			if (x + dist > 7)
				break;
			if (board.getBoard()[x + dist][y].get_team() == team)
				break;
			int[] move = {x + dist, y};
			moves.add(move);
			if (board.getBoard()[x + dist][y].get_team() == (team+1)%2)
				break;
		}
		for (int dist = 1; dist < 8; dist ++) {
			if (y + dist > 7)
				break;
			if (board.getBoard()[x][y + dist].get_team() == team)
				break;
			int[] move = {x, y + dist};
			moves.add(move);
			if (board.getBoard()[x][y + dist].get_team() == (team+1)%2)
				break;
		}
		for (int dist = 1; dist < 8; dist ++) {
			if (x - dist < 0 )
				break;
			if (board.getBoard()[x - dist][y].get_team() == team)
				break;
			int[] move = {x - dist, y};
			moves.add(move);
			if (board.getBoard()[x - dist][y].get_team() == (team+1)%2)
				break;
		}
		for (int dist = 1; dist < 8; dist ++) {
			if (y - dist < 0)
				break;
			if (board.getBoard()[x][y - dist].get_team() == team)
				break;
			int[] move = {x, y - dist};
			moves.add(move);
			if (board.getBoard()[x][y - dist].get_team() == (team+1)%2)
				break;
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
		int x_dist = kingx - x;
		int y_dist = kingy - y;
		if (Math.abs(x_dist) == Math.abs(y_dist)) {
			int x_dir = x_dist/Math.abs(x_dist);
			int y_dir = y_dist/Math.abs(y_dist);
			for (int i = 1; i < x_dist; i ++)
				for (int j = 1; j < y_dist; j++) {
					if (board.getBoard()[x + x_dir*i][y + y_dir*j].get_team() != -1)
						return false;
				}
			return true;
		}
		if (y_dist == 0) {
			int x_dir = x_dist/Math.abs(x_dist);
			for (int i = 1; i < x_dist; i ++) {
				if (board.getBoard()[x + i*x_dir][y].get_team() != -1)
					return false;
			}	
			return true;
		}
		if (x_dist == 0) {
			int y_dir = y_dist/Math.abs(y_dist);
			for (int i = 1; i < y_dist; i ++) {
				if (board.getBoard()[x][y + i*y_dir].get_team() != -1)
					return false;
			}
			return true;
		}
		return false;
	}

}
