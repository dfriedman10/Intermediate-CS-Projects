
public class Board {
	private Piece[][] board = new Piece[8][8];
	private int[][] kings = new int[2][2];
	
	public Board() {
		board[3][7] = new King(1,-1,"BlackKing");
		board[3][0] = new King(0,-1,"WhiteKing");
		kings[0][0] = 3;
		kings[0][1] = 0;
		kings[1][0] = 3;
		kings[1][1] = 7;
		
		board[0][0] = new Rook(0,5,"WhiteRook1");
		board[1][0] = new Knight(0,3,"WhiteKnight1");
		board[2][0] = new Bishop(0,3,"WhiteBishop1");
		board[4][0] = new Queen(0,9,"WhiteQueen");
		board[5][0] = new Bishop(0,3,"WhiteBishop2");
		board[6][0] = new Knight(0,3,"WhiteKnight2");
		board[7][0] = new Rook(0,5,"WhiteRook2");
		
		board[0][7] = new Rook(1,5,"BlackRook1");
		board[1][7] = new Knight(1,3,"BlackKnight1");
		board[2][7] = new Bishop(1,3,"BlackBishop1");
		board[4][7] = new Queen(1,9,"BlackQueen");
		board[5][7] = new Bishop(1,3,"BlackBishop2");
		board[6][7] = new Knight(1,3,"BlackKnight2");
		board[7][7] = new Rook(1,5,"BlackRook2");
			
		for (int i = 0; i < 8; i ++) 
			board[i][1] = new Pawn(0,1,"WhitePawn" + i);
		for (int i = 0; i < 8; i ++) 
			board[i][6] = new Pawn(1,1,"BlackPawn" + i);

		for (int i = 0; i < 8; i++) {
			for (int j = 2; j <= 5; j ++) {
				board[i][j] = new Empty();
			}
		}
	}
	public Board(Piece[][] b) {
		board = b;
	}
	
	public Piece[][] getBoard() {
		return board;
	}
	public Board copy() {
		Piece[][] b = new Piece[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				b[i][j] = board[i][j];
			}
		}
		return new Board(b);
	}
	public void move(int x, int y, int newx, int newy) {
		if (board[x][y].value == -1) {
			kings[board[x][y].team][0] = newx;
			kings[board[x][y].team][1] = newy;
		}
		board[newx][newy] = board[x][y];
		board[x][y] = new Empty();
	}
	public int score(int turn) {
		int score = 0;
		boolean[] kings = {false, false};
		int[] scales = {1,-1};
		
		// sum the values of the pieces
		for (int i = 0; i < 8; i ++)
			for (int j = 0; j < 8; j ++) {
				int team = board[i][j].get_team();
				int val = board[i][j].get_value();
				int scale = scales[(team+turn)%2];
				if (team > -1) {
					// keep track of the kings 
					if (val == -1)
						kings[team] = true;
					
					// add values of the pieces
					else
						score += val*scale*100;
					
					// add value for pawn support
					if (val == 1) {
						if (team == 0 && i < 7 && j < 7 && board[i+1][j+1].get_team() == team)
							score += 5*scale;
						if (team == 0 && i > 0 && j < 7 && board[i-1][j+1].get_team() == team)
							score += 5*scale;
						if (team == 1 && i < 7 && j >0 && board[i+1][j-1].get_team() == team)
							score += 5*scale;
						if (team == 1 && i > 0 && j >0 && board[i-1][j-1].get_team() == team)
							score += 5*scale;
					}
					
					// add value for center control
					if (i >= 2 && i <= 5 && j >= 2 && j <= 5)
						score += 10*scale;
				}
			}
		
		
		
		if (!kings[turn])
			return Integer.MIN_VALUE;
		else if (!kings[(turn+1)%2])
			return Integer.MAX_VALUE;
		return score;
	}
	
	public boolean check() {
		for (int i = 0; i < 8; i ++)
			for (int j = 0; j < 8; j++) {
				if (board[i][j].get_team()!= -1) {
					int opp = (board[i][j].get_team()+1)%2;
					for (int[] moves : board[i][j].get_moves(this, i, j)) 
						if (moves[0] == kings[opp][0] && moves[1] == kings[opp][1]) {
							System.out.println("Check!");
							return true;
						}
				}
					
			}
		return false;
	}
	
	public boolean checkMate() {
		boolean[] kings = {true,true};
		for (int i = 0; i < 8; i ++)
			for (int j = 0; j < 8; j++) {
				if (board[i][j].get_value() == -1) {
					kings[board[i][j].get_team()] = false;
				}
			}
		return kings[0] || kings[1];
	}
	
	public String toString() {
		String s = "";
		for (Piece[] r : board) {
			for (Piece p : r) {
				if (p.name.equals("Empty"))
					s += "          ";
				else 
					s += p.name+"   ";
			}
			s += "\n";
		}
		return s;
	}
}
