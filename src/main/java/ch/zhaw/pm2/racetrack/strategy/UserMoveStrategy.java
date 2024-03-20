package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Config;
import model.Direction;

import org.beryx.textio.TextIO;

/**
 * Let the user decide the next move.
 */
public class UserMoveStrategy implements MoveStrategy {

    private TextIO textIO;
    private char carId;

    /**
     This constructor initializes a UserMoveStrategy object with a TextIO object for user input and a car ID.
     @param textIO The TextIO object to be used for user input.
     @param carId The ID of the car associated with this UserMoveStrategy object.
     */
    public UserMoveStrategy(TextIO textIO, char carId) {
        this.textIO = textIO;
        this.carId = carId;
    }

    /**
     * {@inheritDoc}
     * Asks the user for the direction vector.
     *
     * @return next direction, null if the user terminates the game.
     */
    @Override
    public Direction nextMove() {
        return textIO.newEnumInputReader(Direction.class).read(Config.OutputMessage.DIRECTION.toString() + carId);
    }
}
