package ch.zhaw.pm2.racetrack.strategy;

import java.io.FileNotFoundException;

import ch.zhaw.pm2.racetrack.Track;
import ch.zhaw.pm2.racetrack.pathfinder.PathFinder;
import model.Direction;
import model.PositionVector;

/**
 * Determines the next move based on a file containing points on a path.
 */
public class PathFinderMoveStrategy implements MoveStrategy {

	private PathFollower pathFollower;

	public PathFinderMoveStrategy(PositionVector startPosition, Track track) throws FileNotFoundException {
		pathFollower = new PathFollower(startPosition, new PathFinder(startPosition, track).getShortestPath());
	}
	
    /**
     * {@inheritDoc}
     *
     * @return next direction to find the quickest path, {@link Direction#NONE} if there are no more coordinates available
     */
    @Override
    public Direction nextMove() {
    	return pathFollower.nextMove();
    }
}
