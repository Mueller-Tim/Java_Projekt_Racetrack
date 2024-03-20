package ch.zhaw.pm2.racetrack.strategy;

import org.junit.jupiter.api.Test;

import model.PositionVector;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The PathFollowerMoveStrategyTest tests the PathFollowerStrategy class with jUnit tests.
 */
public class PathFollowerMoveStrategyTest {
	private final static File DEFAULT_FILE = new File("follower/challenge_handout_points.txt");

	/**
	 * description: the file has an invalid format
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: negative test
	 * input: PathFollowerStrategy is initialised with a file, which has an invalid format
	 * output: IllegalArgumentException
	 */
	@Test
	void invalidFollowerFileException() {
		String pathname = "src/test/java/ch/zhaw/pm2/racetrack/testresources/follower-file-invalid-format.txt";
		assertThrows(IllegalArgumentException.class, () -> new PathFollowerMoveStrategy(new File(pathname), new PositionVector(0, 0)));
	}
}
