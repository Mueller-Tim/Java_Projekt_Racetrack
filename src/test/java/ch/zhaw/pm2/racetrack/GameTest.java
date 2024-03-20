package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.UserMoveStrategy;
import exceptions.InvalidFileFormatException;
import model.Direction;
import model.PositionVector;
import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The GameTest class contains JUnit tests for testing the functionality of the
 * Game class
 */
class GameTest {
	private Track track;
	private Game testee;

	private static final TextIO textIO = TextIoFactory.getTextIO();
	private static final TextTerminal<?> textTerminal = textIO.getTextTerminal();

	@BeforeEach
	void init() {
		try {
			track = new Track(new File("tracks/oval-clock-up.txt"));
		} catch (IOException | InvalidFileFormatException e) {
			e.printStackTrace();
		}
		testee = new Game(track);

	}

	/**
	 * description: test if the correct number of cars gets returned
	 * equivalence class: 1
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: nothing
	 * output: should be 2
	 */
	@Test
	void getCarCount() {
		assertEquals(2, testee.getCarCount());
	}

	/**
	 * description: test if car switching works (correct car index gets returned)
	 * equivalence class: 1
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: game tries to switch to the next car
	 * output: current car index should match the argument
	 */
	@Test
	void switchingToNextCar() {
		assertEquals(0, testee.getCurrentCarIndex());
		testee.switchToNextActiveCar();
		assertEquals(1, testee.getCurrentCarIndex());
		testee.switchToNextActiveCar();
		assertEquals(0, testee.getCurrentCarIndex());
	}


	/**
	 * description: test if car switching works, even if one car crashed
	 * equivalence class: 2
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: game tries to switch to the next car
	 * output: current car index should match the argument
	 */
	@Test
	void switchToNextActiveCar() {
		testee.doCarTurn(Direction.LEFT);
		testee.doCarTurn(Direction.LEFT);
		testee.switchToNextActiveCar();
		int activeCarId = testee.getCurrentCarIndex();
		assertEquals(activeCarId, testee.getCurrentCarIndex());
	}


	/**
	 * description: test if the game has no winner, with at least two reaming cars and
	 * no car passed the finish line
	 * equivalence class: 1
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: game tries to get the winner
	 * output: it should not have a winner yet
	 */
	@Test
	void getNoWinner() {
		assertEquals(testee.NO_WINNER, testee.getWinner());
	}

	/**
	 * description: test if the game has a winner, because only one car remains
	 * equivalence class: 2
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: games tries to get the winner
	 * output: the winner is the remaining car
	 */
	@Test
	void getWinnerLastCar() {
		testee.doCarTurn(Direction.LEFT);
		testee.doCarTurn(Direction.LEFT);
		testee.switchToNextActiveCar();
		assertEquals(testee.getCurrentCarIndex(), testee.getWinner());
	}

	/**
	 * description: test if the game has a winner, if a car drives over the finish line
	 * equivalence class: 3
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: games tries to get the winner
	 * output: the winner is the car, which drove over the finish line
	 */
	@Test
	void getWinnerOverFinishLine() {
		testee.getTrack().getCar(0).incrementLapCounter();
		assertEquals(0, testee.getWinner());
	}

	/**
	 * description: test if crash detection works as expected
	 * equivalence class: 1
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: nothing
	 * output: crash status of car should match argument
	 */
	@Test
	void testCrashDetection() {
		assertFalse(testee.getTrack().getCar(0).isCrashed());
		assertFalse(testee.getTrack().getCar(1).isCrashed());

		testee.doCarTurn(Direction.RIGHT);
		testee.switchToNextActiveCar();
		testee.doCarTurn(Direction.LEFT);
		testee.switchToNextActiveCar();
		testee.doCarTurn(Direction.RIGHT);
		testee.switchToNextActiveCar();

		assertTrue(testee.getTrack().getCar(0).isCrashed());
		assertFalse(testee.getTrack().getCar(1).isCrashed());
	}

	/**
	 * description: test if checking for moving cars work as expected
	 * equivalence class: 1
	 * initial condition: initialized testee with track containing oval-clock-up.txt plus two cars with
	 * UserMoveStrategy
	 * type: positive test
	 * input: nothing
	 * output: true
	 */
	@Test
	void checkForMovingCarsUserMoveStrategy() {
		track.getCar(0).setMoveStrategy(new UserMoveStrategy(textIO, track.getCar(0).getId()));
		track.getCar(1).setMoveStrategy(new UserMoveStrategy(textIO, track.getCar(1).getId()));
		assertTrue(testee.checkForMovingCars());
	}

	/**
	 * description: test if checking for moving cars work as expected
	 * equivalence class: 2
	 * initial condition: initialized testee with track containing oval-clock-up.txt plus two cars with
	 * DoNotMoveStrategy
	 * type: negativ test
	 * input: nothing
	 * output: false
	 */
	@Test
	void checkForMovingCarsDoNotMoveStrategy() {
		track.getCar(0).setMoveStrategy(new DoNotMoveStrategy());
		track.getCar(1).setMoveStrategy(new DoNotMoveStrategy());
		assertFalse(testee.checkForMovingCars());
	}

	/**
	 * description: test if checking for moving cars work as expected
	 * equivalence class: 4
	 * initial condition: initialized testee with track containing oval-clock-up.txt plus two cars with
	 * UserMoveStrategy. One of the cars crash during the race.
	 * type: positive test
	 * input: nothing
	 * output: true
	 */
	@Test
	void checkForMovingCarsOneCrashed() {
		track.getCar(0).setMoveStrategy(new UserMoveStrategy(textIO, track.getCar(0).getId()));
		track.getCar(1).setMoveStrategy(new UserMoveStrategy(textIO, track.getCar(1).getId()));
		track.getCar(0).crash(new PositionVector(1, 1));
		assertTrue(testee.checkForMovingCars());
	}

	/**
	 * description: test if checking for moving cars work as expected
	 * equivalence class: 4
	 * initial condition: initialized testee with track containing oval-clock-up.txt plus two cars with
	 * UserMoveStrategy. Both of the cars crash during the race.
	 * type: negativ test
	 * input: nothing
	 * output: false
	 */
	@Test
	void checkForMovingCarsAllCrashed() {
		track.getCar(0).setMoveStrategy(new UserMoveStrategy(textIO, track.getCar(0).getId()));
		track.getCar(1).setMoveStrategy(new UserMoveStrategy(textIO, track.getCar(1).getId()));
		track.getCar(0).crash(new PositionVector(1, 1));
		track.getCar(1).crash(new PositionVector(1, 1));
		assertFalse(testee.checkForMovingCars());
	}

	/**
	 * description: test if a diagonal path is correct calculated
	 * equivalence class: 1
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: try to calculate the diagonal path
	 * output: should contain all vectors between startPosition and endPosition,
	 *         inclusive both position vectors
	 */
	@Test
	void calculatePathDiagonal(){
		PositionVector startPosition = new PositionVector(1,2);
		PositionVector endPosition = new PositionVector(3,4);
		List<PositionVector> expectedPath = new ArrayList<>();
		expectedPath.add(startPosition);
		expectedPath.add(new PositionVector(2,3));
		expectedPath.add(endPosition);
		assertEquals(expectedPath, testee.calculatePath(startPosition, endPosition));
	}

	/**
	 * description: test if a horizontal path is correct calculated
	 * equivalence class: 2
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: try to calculate the diagonal path
	 * output: should contain all vectors between startPosition and endPosition,
	 *         inclusive both position vectors
	 */
	@Test
	void calculatePathHorizontal(){
		PositionVector startPosition = new PositionVector(1,2);
		PositionVector endPosition = new PositionVector(5,2);
		List<PositionVector> expectedPath = new ArrayList<>();
		expectedPath.add(startPosition);
		expectedPath.add(new PositionVector(2,2));
		expectedPath.add(new PositionVector(3,2));
		expectedPath.add(new PositionVector(4,2));
		expectedPath.add(endPosition);
		assertEquals(expectedPath, testee.calculatePath(startPosition, endPosition));

	}

	/**
	 * description: test if a vertical path is correct calculated
	 * equivalence class: 3
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: try to calculate the diagonal path
	 * output: should contain all vectors between startPosition and endPosition,
	 *         inclusive both position vectors
	 */
	@Test
	void calculatePathVertical(){
		PositionVector startPosition = new PositionVector(1,2);
		PositionVector endPosition = new PositionVector(1,5);
		List<PositionVector> expectedPath = new ArrayList<>();
		expectedPath.add(startPosition);
		expectedPath.add(new PositionVector(1,3));
		expectedPath.add(new PositionVector(1,4));
		expectedPath.add(endPosition);
		assertEquals(expectedPath, testee.calculatePath(startPosition, endPosition));

	}

	/**
	 * description: test if the path is correct calculated, even if the startPosition
	 *              and the endPosition are the same
	 * equivalence class: 1
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: try to calculate the diagonal path
	 * output: should contain only one vector, the startPosition or the endPosition
	 */
	@Test
	void calculatePathSamePosition(){
		PositionVector startPosition = new PositionVector(1,2);
		PositionVector endPosition = new PositionVector(1,2);
		List<PositionVector> expectedPath = new ArrayList<>();
		expectedPath.add(startPosition);
		assertEquals(expectedPath, testee.calculatePath(startPosition, endPosition));
	}

	/**
	 * description: test if a path is correct calculated, even if the startPosition is further
	 *              away than the endPosition
	 * equivalence class: 5
	 * initial condition: initialized testee with track containing oval-clock-up.txt
	 * type: positive test
	 * input: try to calculate the diagonal path
	 * output: should contain all vectors between startPosition and endPosition,
	 *         inclusive both position vectors
	 */
	@Test
	void calculatePathReverseOrder(){
		PositionVector startPosition = new PositionVector(5,5);
		PositionVector endPosition = new PositionVector(1,1);
		List<PositionVector> expectedPath = new ArrayList<>();
		expectedPath.add(startPosition);
		expectedPath.add(new PositionVector(4,4));
		expectedPath.add(new PositionVector(3,3));
		expectedPath.add(new PositionVector(2,2));
		expectedPath.add(endPosition);
		assertEquals(expectedPath, testee.calculatePath(startPosition, endPosition));

	}

	/**
	 * description: test if car makes a car turn
	 * equivalence class: 1
	 * initial condition: initialized testee with track containing oval-clock-up.txt and car makes
	 *                   one car turn to the right
	 * type: positive test
	 * input: makes another car turn to the right
	 * output: the new position should be two point to the right on the x-axis, because the velocity of both
	 *        turns are added to getter.
	 */
	@Test
	void doCarTurn(){
		testee.doCarTurn(Direction.RIGHT);
		int xNextValuePosition = testee.getCarPosition(0).getX() + 2;
		int yNextValuePosition = testee.getCarPosition(0).getY() + 0;
		testee.doCarTurn(Direction.RIGHT);
		assertEquals(new PositionVector(xNextValuePosition,yNextValuePosition), testee.getCarPosition(0));
	}
}