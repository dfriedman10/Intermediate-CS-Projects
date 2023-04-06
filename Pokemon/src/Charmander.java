
public class Charmander extends Pokemon{

	public Charmander() {
		super("Charmander", Type.FIRE, 100, "charmander.png");
	}

	@Override
	public Move[] generateMoves() {
		
		Move[] moves = new Move[4];
		moves[0] = new Move("ember", Type.FIRE, 30, Effect.NONE);
		moves[1] = new Move("scratch", Type.NORMAL, 15, Effect.NONE);
		moves[2] = new Move("psychic", Type.WATER, 30, Effect.CONFUSED);
		moves[3] = new Move("flamethrower", Type.FIRE, 70, Effect.NONE);
		
		return moves;
	}

}
