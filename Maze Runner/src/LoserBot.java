
import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class LoserBot extends Bot {

    private final MazeRunner mr;

//    private MovingState state;
//    private boolean lastMove;
    private boolean success;
    private State state;

    public LoserBot(MazeRunner mr) {
        super(mr, Color.GREEN);
        this.mr = mr;
//        this.state = MovingState.IDLE;
        this.state = State.FORWARD;
    }

    @Override
    public void move() {
        switch(state) {
            case FORWARD: {
                this.success = moveForward();
                this.state = State.CHECK;
                break;
            }
            case CHECK: {
                if(success) {
                    turnLeft();
                    this.state = State.FORWARD;
                } else {
                    // need to turn left 3 times
                    turnLeft();
                    this.state = State.ROTATION_2;
                }
                break;
            }
            case ROTATION_2: {
                turnLeft();
                this.state = State.ROTATION_3;
                break;
            }
            case ROTATION_3: {
                turnLeft();
                this.state = State.FORWARD;
                break;
            }
        }
    }

    private enum State {
        CHECK,
        ROTATION_2,
        ROTATION_3,
        FORWARD
    }

    //    @Override
//    public void move() {
//        switch(this.state) {
//            case IDLE: {
//                this.lastMove = moveForward();
//                this.state = MovingState.FORWARD;
//                break;
//            }
//            case CHECKING: {
//                this.lastMove = moveForward();
//                this.state = MovingState.EVALUATING;
//                break;
//            }
//            case EVALUATING: {
//                if(this.lastMove) {
//                    this.state = MovingState.IDLE;
//                    this.lastMove = moveForward();
//                } else {
//                    // need to turn 3 times if false
//                    this.state = MovingState.TURN_STEP_3;
//                    turnLeft();
//                }
//                break;
//            }
//            case TURN_STEP_1: {
//                turnLeft();
//                this.state = MovingState.TURN_STEP_2;
//                break;
//            }
//            case TURN_STEP_2: {
//                turnLeft();
//                this.state = MovingState.TURN_STEP_3;
//                break;
//            }
//            case TURN_STEP_3: {
//                turnLeft();
//                this.state = MovingState.IDLE;
//                break;
//            }
//            case FORWARD: {
//                if(lastMove) {
//                    this.lastMove = moveForward();
//                } else {
//                    turnLeft();
//                    this.state = MovingState.CHECKING;
//                }
//                break;
//            }
//        }
//    }

//    private enum MovingState {
//        IDLE,
//        CHECKING,
//        EVALUATING,
//        TURN_STEP_1,
//        TURN_STEP_2,
//        TURN_STEP_3,
//        FORWARD
//    }
}
