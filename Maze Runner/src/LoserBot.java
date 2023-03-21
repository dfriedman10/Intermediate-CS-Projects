
import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class LoserBot extends Bot {


    public LoserBot(MazeRunner mr) {
        super(mr, Color.GREEN);
    }

    @Override
    public void move() {

    	if (!moveForward()) turnLeft();
    }
}
