
public class Squirtle extends Pokemon{

	public Squirtle() {
		super("Squirtle", Type.WATER, 80, "squirtle.png");
	}

	@Override
	public Move[] generateMoves() {
		
		Move[] moves = new Move[4];
		moves[0] = new Move("water gun", Type.WATER, 30, Effect.NONE);
		moves[1] = new Move("tackle", Type.NORMAL, 15, Effect.NONE);
		moves[2] = new Move("boredom", Type.GRASS, 0, Effect.ASLEEP);
		moves[3] = new Move("hydro pump", Type.WATER, 100, Effect.NONE);
		
		return moves;
	}

}
