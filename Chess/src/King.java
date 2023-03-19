import java.awt.Graphics;
import java.util.ArrayList;

public class King extends Piece{
	
	public King(int turn, int v, String piece_name) {
		super(turn,v,piece_name);
		value = -1;
	}

	@Override
	public ArrayList<int[]> get_moves(Board board, int x, int y) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		for (int i = -1; i < 2; i ++)
			for (int j = -1; j < 2; j ++) {
				if (x + i >= 0 && x + i < 8 && y + j < 8 && y + j >= 0) 
					if (board.getBoard()[x + i][y + j].get_team() != team) {
						int[] move = {x+i,y+j};
						moves.add(move);
					}
						
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
		// TODO Auto-generated method stub
		return false;
	}

}
