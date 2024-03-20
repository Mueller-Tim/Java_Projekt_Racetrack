package ch.zhaw.pm2.racetrack.strategy;

import model.Direction;
import model.PositionVector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Determines the next move based on a file containing points on a path.
 */
public class PathFollowerMoveStrategy implements MoveStrategy {
	private List<PositionVector> wayPoints = new ArrayList<>();
	private PathFollower pathFollower;

	public PathFollowerMoveStrategy(File followerFile, PositionVector startPosition) throws FileNotFoundException {
		extractPositionVectorsFromFile(followerFile);
		pathFollower = new PathFollower(startPosition, wayPoints);
	}

	private void extractPositionVectorsFromFile(File followerFile) throws FileNotFoundException {
		Scanner fileScanner = new Scanner(followerFile);
		while (fileScanner.hasNext()) {
			String currentLine = fileScanner.nextLine();
			if (currentLine.matches("^\\(X:\\d+, Y:\\d+\\)$")) {
				String[] stringSplit = currentLine.split(":");
				wayPoints.add(new PositionVector(Integer.parseInt(stringSplit[1].split(",")[0]),
						Integer.parseInt(stringSplit[2].split("\\)")[0])));
			} else {
				throw new IllegalArgumentException("The file " + followerFile.getAbsolutePath()
						+ " contains unacceptable content for PathFollowerMoveStrategy.");
			}
		}
		fileScanner.close();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return next direction to follow the given path, {@link Direction#NONE} if
	 *         there are no more coordinates available
	 */
	@Override
	public Direction nextMove() {
		return pathFollower.nextMove();
	}
}