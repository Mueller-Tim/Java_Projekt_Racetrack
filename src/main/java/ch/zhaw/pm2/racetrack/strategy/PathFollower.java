package ch.zhaw.pm2.racetrack.strategy;

import java.io.FileNotFoundException;
import java.util.List;

import model.Direction;
import model.PositionVector;

/**
 * The PathFollower Class returns Directions via the nextMove Method which allow travel between the provided waypoints
 *
 */
public class PathFollower {
	private List<PositionVector> wayPoints;
	private int movePointer = 0;
	private PositionVector currentPosition;
	private PositionVector currentVelocity;

	public PathFollower(PositionVector startPosition, List<PositionVector> path) throws FileNotFoundException {
		currentPosition = startPosition;
		currentVelocity = new PositionVector(0, 0);
		wayPoints = path;
	}

	/**
	 * Fetches the next direction to apply to a car to reach the next wayPoint 
	 * @return Direction direction
	 */
	public Direction nextMove() {
		PositionVector target = wayPoints.get(movePointer);
		if (currentPosition.equals(target)) {
			movePointer++;
		}
		int accX = calculateAcceleration(currentPosition.getX(), wayPoints.get(movePointer).getX(),
				currentVelocity.getX());
		int accY = calculateAcceleration(currentPosition.getY(), wayPoints.get(movePointer).getY(),
				currentVelocity.getY());
		Direction direction = selectDirection(new PositionVector(accX, accY));
		currentVelocity = currentVelocity.add(direction.vector);
		currentPosition = currentPosition.add(currentVelocity);
		return direction;
	}

	private int calculateAcceleration(int current, int target, int currentVelocity) {
		int factor = target >= current ? 1 : -1;
		int distanceRemaining = Math.abs(target - current);
		int v = Math.abs(currentVelocity);
		if ((v + 1) * 2 <= distanceRemaining || distanceRemaining > 0 && v == 0) {
			return 1 * factor;
		} else if (v * 2 > distanceRemaining) {
			return currentVelocity > 0 ? -1 : 1;
		} else {
			return 0;
		}
	}

	private Direction selectDirection(PositionVector acceleration) {
		for (Direction direction : Direction.values()) {
			if (direction.vector.equals(acceleration)) {
				return direction;
			}
		}
		throw new IllegalArgumentException("invalid acceleration");
	}
}
