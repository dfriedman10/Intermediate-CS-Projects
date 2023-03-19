import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

public abstract class Piece {
	
	protected int team, value;
	protected String name;
	
	public Piece(int turn, int v, String piece_name) {
		team = turn;
		name = piece_name;
		value = v;
	}
	public int get_value() {
		return value;  
	}
	public abstract ArrayList<int[]> get_moves(Board board, int x, int y);
	
	public int get_team() {
		return team;
	}
	
	public String get_name() {
		return name;
	}
	
	public abstract boolean is_empty();
	
	public abstract boolean check(int kingx, int kingy, int x, int y, Board board);


}
