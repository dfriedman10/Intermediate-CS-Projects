import java.util.ArrayList;
public class GameTree  {

	private GameTree parent;
	protected ArrayList<GameTree> children;
	private Board board;
	private int turn;
	private final int MAXDEPTH = 3;
	private int minMax;
	
	public GameTree(Board input, int turn_input, int maxmin) {
		board = input;
		parent = null;
		turn = turn_input;
		children = new ArrayList<GameTree>();
		minMax = maxmin;
	}
	
	public GameTree(Board input,int turn_input, GameTree parent_input, int maxmin) {
		board = input;
		turn = turn_input;
		parent = parent_input;
		children = new ArrayList<GameTree>();
		parent.add_child(this);
		minMax = maxmin;
	}
	
	public void add_child(GameTree child) {
		children.add(child);
		child.parent = this;
	}
	
	public GameTree getParent() {
		return parent;
	}	
	
	public ArrayList<GameTree> getChildren() {
		return children;
	}

	/*
	public int[] best_move_starter2() {
		int maxScore = Integer.MIN_VALUE;
		int[] bestmove = new int[4];
		
		for (int i = 0; i < 8; i ++) 
			for (int j = 0; j < 8; j ++) {
				Piece piece = board.getBoard()[i][j];
				if (piece.get_team() == turn)
					for (int[] move : piece.get_moves(board, i, j)) {
						Board boardcopy = board.copy();
						boardcopy.move(i, j, move[0], move[1]);
						//System.out.println(boardcopy);
						GameTree child = new GameTree(boardcopy, turn, this, Integer.MAX_VALUE);
						this.add_child(child);
						int score = -1*best_move2(1,boardcopy);
						if (score > maxScore) {
							int[] temp = {i,j,move[0],move[1]};
							bestmove = temp;
							maxScore = score;
						}
					}
			}	
		System.out.println(maxScore+"\n");
		return bestmove;
	}
	// returns {piece x, piece y, move x, move y}
	public int best_move2(int depth, Board b) {
		//System.out.println(x++);
		int maxScore = Integer.MIN_VALUE;
		
		int s = b.score(turn);
		if (depth == MAXDEPTH || s == Integer.MAX_VALUE)
			return s;
			
		for (int i = 0; i < 8; i ++) 
			for (int j = 0; j < 8; j ++) {
				Piece piece = b.getBoard()[i][j];
				if (piece.get_team() == (turn + depth)%2)
					for (int[] move : piece.get_moves(b, i, j)) {
						Board boardcopy = b.copy();
						boardcopy.move(i, j, move[0], move[1]);

						//System.out.println(boardcopy);
						int score;
						GameTree child = new GameTree(boardcopy, turn, this, 7777777);
						this.add_child(child);
						score = -1*best_move2(depth+1,boardcopy);
						if (score > maxScore) 
							maxScore = score;
					}
			}	
		return maxScore;
	}*/
	
	public int[] best_move_starter() {
		int[] bestmove = new int[4];
		int max = Integer.MIN_VALUE;
		
		for (int i = 0; i < 8; i ++) 
			for (int j = 0; j < 8; j ++) {
				Piece piece = board.getBoard()[i][j];
				if (piece.get_team() == turn)
					for (int[] move : piece.get_moves(board, i, j)) {
						Board boardcopy = board.copy();
						boardcopy.move(i, j, move[0], move[1]);
						//System.out.println(boardcopy);
						GameTree child = new GameTree(boardcopy, turn, this, Integer.MAX_VALUE);
						this.add_child(child);
						child.best_move(1);
						if (child.minMax > max) {
							int[] temp = {i,j,move[0],move[1]};
							bestmove = temp;
							max = child.minMax;
							//System.out.println("depth 0: " + max);
						}
					}
			}	
		System.out.println(max+"\n");
		return bestmove;
	}
	// returns {piece x, piece y, move x, move y}
	public void best_move(int depth) {
		
		int s = board.score(turn);
		if (depth == MAXDEPTH) {
			if (depth%2 == 1 && s > parent.minMax) {
				parent.minMax = s;
				// prune
				//System.out.println("depth(max) " + depth+": "+s);
			}
			else if (depth %2 == 0 && s < parent.minMax) {
				parent.minMax = s;
				// prune
				//System.out.println("depth(max) " + depth+": "+s);
			}
			return;
		}
			/* FIND CHECKMATE
		if (s == Integer.MAX_VALUE && depth%2 == 1) {
			parent.minMax = s;
			// do pruning
			System.out.println("depth(mate) " + depth+": "+s);
			return;
		}
		else if (s == Integer.MIN_VALUE && depth%2 == 0) {
			parent.minMax = s;
			//pruning
			System.out.println("depth(mate) " + depth+": "+s);
			return;
		}
			*/
		for (int i = 0; i < 8; i ++) 
			for (int j = 0; j < 8; j ++) {
				Piece piece = board.getBoard()[i][j];
				if (piece.get_team() == (turn + depth)%2)
					for (int[] move : piece.get_moves(board, i, j)) {
						Board boardcopy = board.copy();
						boardcopy.move(i, j, move[0], move[1]);

						//System.out.println(boardcopy);
						GameTree child;
						if (depth%2 == 1)
							child = new GameTree(boardcopy, turn, this, Integer.MIN_VALUE);
						else 
							child = new GameTree(boardcopy, turn, this, Integer.MAX_VALUE);
						this.add_child(child);
						child.best_move(depth+1);
					}
			}
		if (minMax < parent.minMax && depth%2 == 0) {
			parent.minMax = minMax;
			//System.out.println("depth(mid) " + depth+": "+minMax);
		}
		else if (minMax > parent.minMax && depth%2 == 1) {
			parent.minMax = minMax;
			//System.out.println("depth(mid) " + depth+": "+minMax);
		}
	}
}
