package ch.zhaw.pm2.racetrack.strategy;

import exceptions.InvalidFileFormatException;
import model.Direction;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/***
 * The MoveListStrategyTest class contains JUnit tests for testing the MoveListStrategy class.
 */
class MoveListStrategyTest {

	private MoveListStrategy moveListStrategy;
	private final static File DEFAULT_FILE = new File("moves/challenge-car-a.txt");


	/**
	 * description: the file could not be found
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: negative test
	 * input: MoveListStrategy is initialised with a file, which could nod be found
	 * output: FileNotFoundException
	 */
	@Test
	void MoveListStrategyNoFile(){
		assertThrows(FileNotFoundException.class, () -> new MoveListStrategy(new File("NotAvailable-File")));
	}

	/**
	 * description: the file has an invalid format
	 * equivalence class: 2
	 * initial condition: nothing
	 * type: negative test
	 * input: MoveListStrategy is initialised with a file, which has an invalid format
	 * output: InvalidFileFormatException
	 */
	@Test
	void MoveListStrategyInvalidFormat(){
		String pathname = "src/test/java/ch/zhaw/pm2/racetrack/testresources/moveInvalidFormat.txt";
		assertThrows(InvalidFileFormatException.class, () -> new MoveListStrategy(new File(pathname)));
	}

	/**
	 * description: the file has a valid format
	 * equivalence class: 3
	 * initial condition: nothing
	 * type: positive test
	 * input: MoveListStrategy is initialised with a valid file
	 * output: it should not throw any exception
	 */
	@Test
	void MoveListStrategy(){
		assertDoesNotThrow(() -> new MoveListStrategy(DEFAULT_FILE));
	}


	/**
	 * description: first move direction of the file
	 * equivalence class: 1
	 * initial condition: initialize MoveListStrategy with a valid file
	 * type: positive test
	 * input: moveListStrategy makes the first move
	 * output: should display the first DIRECTION of the file
	 */
	@Test
	void nextMoveFirstDirection() throws InvalidFileFormatException, FileNotFoundException {
		moveListStrategy = new MoveListStrategy(DEFAULT_FILE);
		Direction firstDirection = moveListStrategy.getMovesList().get(0);
		assertEquals(firstDirection, moveListStrategy.nextMove());
	}

	/**
	 * description: last move direction of the file
	 * equivalence class: 2
	 * initial condition: initialize MoveListStrategy with a valid file
	 * type: positive test
	 * input: moveListStrategy makes the last move of the file
	 * output: should display the last DIRECTION of the file
	 */
	@Test
	void nextMoveLastDirection() throws InvalidFileFormatException, FileNotFoundException {
		moveListStrategy = new MoveListStrategy(DEFAULT_FILE);
		Direction LastDirection = moveListStrategy.getMovesList().get(moveListStrategy.getMovesList().size()-1);
		for(int i = 0; i < moveListStrategy.getMovesList().size()-1; i++){
			moveListStrategy.nextMove();
		}
		assertEquals(LastDirection, moveListStrategy.nextMove());
	}

	/**
	 * description: infinite move direction after the last line of the file
	 * equivalence class: 3
	 * initial condition: initialize MoveListStrategy with a valid file
	 * type: positive test
	 * input: moveListStrategy makes a move after the last line of the file
	 * output: should display the NONE-DIRECTION
	 */
	@Test
	void nextMoveInfiniteDirection() throws InvalidFileFormatException, FileNotFoundException {
		moveListStrategy = new MoveListStrategy(DEFAULT_FILE);
		for(int i = 0; i < moveListStrategy.getMovesList().size(); i++){
			moveListStrategy.nextMove();
		}
		assertEquals(Direction.NONE, moveListStrategy.nextMove());
	}
}