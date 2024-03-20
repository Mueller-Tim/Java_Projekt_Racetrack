package ch.zhaw.pm2.racetrack.strategy;

import org.junit.jupiter.api.Test;

import model.Direction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The DoNotMoveStrategyTest class contains JUnit tests for testing the functionality of the DoNotMoveStrategy class
 */



class DoNotMoveStrategyTest {

	private DoNotMoveStrategy doNotMoveStrategy = new DoNotMoveStrategy();

	/**
	 * description: test if the method nextMove() always gives the Enum Direction.NONE back
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: positive test
	 * input: nothing
	 * output: Direction.NONE
	 */
	@Test
	void nextMove() {
		assertEquals(Direction.NONE, doNotMoveStrategy.nextMove());
	}
}