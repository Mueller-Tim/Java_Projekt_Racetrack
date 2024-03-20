package ch.zhaw.pm2.racetrack.strategy;

import model.Direction;

/**
 * Do not accelerate in any direction.
 */
public class DoNotMoveStrategy implements MoveStrategy {
    /**
     * {@inheritDoc}
     *
     * @return always {@link Direction#NONE}
     */
    @Override
    public Direction nextMove() {
        return Direction.NONE;
    }
}
