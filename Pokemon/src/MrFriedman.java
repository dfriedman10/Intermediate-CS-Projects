
public class MrFriedman extends Pokemon{

	public MrFriedman() {
		super("Mr. Friedman", Type.GRASS, 60, "MrFriedman.png");
	}

	@Override
	public Move[] generateMoves() {
		
		Move[] moves = new Move[4];
		moves[0] = new Move("fail", Type.NORMAL, 20, Effect.PARALYZED);
		moves[1] = new Move("complicated things", Type.NORMAL, 20, Effect.CONFUSED);
		moves[2] = new Move("project due tomorrow", Type.FIRE, 30, Effect.POISONED);
		moves[3] = new Move("project due tomorrow", Type.FIRE, 30, Effect.POISONED);
		
		return moves;
	}

}
