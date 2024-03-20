package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.MoveListStrategy;
import exceptions.InvalidFileFormatException;
import model.PositionVector;
import model.SpaceType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The TrackTest class contains JUnit tests for testing the functionality of the Track class
 *
 */
class TrackTest {
	private Track testee;

	@BeforeEach
	void init() {
		try {
			testee = new Track(new File("tracks/oval-clock-up.txt"));
		} catch (IOException | InvalidFileFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * description: the track has not a finish line
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: negative test
	 * input: Track is initialised with a track, with no finish line
	 * output: InvalidFileFormatException
	 */
	@Test
	void TrackNoFinishLine(){
		String pathname = "src/test/java/ch/zhaw/pm2/racetrack/testresources/trackNoFinishLine.txt";
		assertThrows(InvalidFileFormatException.class, () -> new Track(new File(pathname)));
	}

	/**
	 * description: the file could not be found
	 * equivalence class: 2
	 * initial condition: nothing
	 * type: negative test
	 * input: Track is initialised with a file, which could nod be found
	 * output: FileNotFoundException
	 */
	@Test
	void TrackNoFile(){
		assertThrows(FileNotFoundException.class, () -> new Track(new File("NotAvailable-File")));
	}

	/**
	 * description: the track has zero cars
	 * equivalence class: 3
	 * initial condition: nothing
	 * type: negative test
	 * input: Track is initialised with a track, with zero cars
	 * output: InvalidFileFormatException
	 */
	@Test
	void TrackZeroCars(){
		String pathname = "src/test/java/ch/zhaw/pm2/racetrack/testresources/trackNoCars.txt";
		assertThrows(InvalidFileFormatException.class, () -> new Track(new File(pathname)));
	}

	/**
	 * description: the track has more than MAX_CARS
	 * equivalence class: 4
	 * initial condition: nothing
	 * type: negative test
	 * input: Track is initialised with a track, with more than MAX_Cars
	 * output: InvalidFileFormatException
	 */
	@Test
	void TrackTooManyCars(){
		String pathname = "src/test/java/ch/zhaw/pm2/racetrack/testresources/trackTooManyCars.txt";
		assertThrows(InvalidFileFormatException.class, () -> new Track(new File(pathname)));
	}

	/**
	 * description: the track contains no track lines
	 * equivalence class: 5
	 * initial condition: nothing
	 * type: negative test
	 * input: Track is initialised with a track, with no track lines
	 * output: InvalidFileFormatException
	 */
	@Test
	void TrackNoLines(){
		String pathname = "src/test/java/ch/zhaw/pm2/racetrack/testresources/trackNoLines.txt";
		assertThrows(InvalidFileFormatException.class, () -> new Track(new File(pathname)));
	}

	/**
	 * description: the track with inconsistent line length
	 * equivalence class: 5
	 * initial condition: nothing
	 * type: negative test
	 * input: Track is initialised with a track, with inconsistent line length
	 * output: InvalidFileFormatException
	 */
	@Test
	void TrackInConsistentLength(){
		String pathname = "src/test/java/ch/zhaw/pm2/racetrack/testresources/trackInconsistentLength.txt";
		assertThrows(InvalidFileFormatException.class, () -> new Track(new File(pathname)));
	}

	/**
	 * description: the track has one car
	 * equivalence class: 7
	 * initial condition: nothing
	 * type: positive test
	 * input: Track is initialised with a track, with one car
	 * output: it should not throw any exception
	 */
	@Test
	void TrackOneCar(){
		String pathname = "src/test/java/ch/zhaw/pm2/racetrack/testresources/trackOneCar.txt";
		assertDoesNotThrow(() -> new Track(new File(pathname)));
	}

	/**
	 * description: the track has the amount of MAX_CARS
	 * equivalence class: 8
	 * initial condition: nothing
	 * type: positive test
	 * input: Track is initialised with a track, with the amount of MAX_CARS
	 * output: it should not throw any exception
	 */
	@Test
	void TrackMaxCars(){
		String pathname = "src/test/java/ch/zhaw/pm2/racetrack/testresources/trackMaxCars.txt";
		assertDoesNotThrow(() -> new Track(new File(pathname)));
	}

	/**
	 * description: test if the height of the track is read correctly from the text file
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: positive test
	 * input: File containing the track oval-clock-up.txt
	 * output: should be 14
	 */
	@Test
	void testCorrectHeightSetting() {
		assertEquals(14, testee.getHeight());
	}
	
	/**
	 * description: test if the width of the track is read correctly from the text file
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: positive test
	 * input: File containing the track oval-clock-up.txt
	 * output: should be 50
	 */
	@Test
	void testCorrectWidthSetting() {
		assertEquals(50, testee.getWidth());
	}

	/**
	 * description: test if the track recognizes all cars from the text file
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: positive test
	 * input: File containing the track oval-clock-up.txt
	 * output: should be 2
	 */
	@Test
	void testCarRecognition() {
		assertEquals(2, testee.getCarCount());
	}

	/**
	 * description: test if the file was read correctly by sampling an instance of every SpaceType
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: positive test
	 * input: File containing the track oval-clock-up.txt
	 * output: should match the provided SpaceType
	 */
	@Test
	void testGetSpaceTypeAtPosition() {
		assertEquals(SpaceType.WALL, testee.getSpaceTypeAtPosition(new PositionVector(0, 0)));
		assertEquals(SpaceType.TRACK, testee.getSpaceTypeAtPosition(new PositionVector(24, 3)));
		assertEquals(SpaceType.FINISH_UP, testee.getSpaceTypeAtPosition(new PositionVector(10, 6)));
		assertEquals(SpaceType.TRACK, testee.getSpaceTypeAtPosition(new PositionVector(8, 5)));
	}

	/**
	 * description: test if the correct char representation gets put out
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: positive test
	 * input: File containing the track oval-clock-up.txt
	 * output: should match the provided chars
	 */
	@Test
	void testCorrectCharRepresentationAtPosition() {
		assertEquals('#', testee.getCharRepresentationAtPosition(0, 0));
		assertEquals(' ', testee.getCharRepresentationAtPosition(3, 24));
		assertEquals('^', testee.getCharRepresentationAtPosition(6, 10));
		assertEquals('a', testee.getCharRepresentationAtPosition(5, 8));
	}

	/**
	 * description: test if the track gets represented as expected
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: positive test
	 * input: File containing the track oval-clock-up.txt
	 * output: should match the provided string
	 */
	@Test
	void testCorrectFieldRepresentation() {
		String expected = "##################################################\n"
				+ "##################################################\n"
				+ "##############                       #############\n"
				+ "##########                              ##########\n"
				+ "#######                                    #######\n"
				+ "######  a   b   #################           ######\n"
				+ "#####^^^^^^^^^^###################           #####\n"
				+ "#####          ###################           #####\n"
				+ "######          #################           ######\n"
				+ "#######                                    #######\n"
				+ "##########                              ##########\n"
				+ "##############                      ##############\n"
				+ "##################################################\n"
				+ "##################################################\n" + "";
		assertEquals(expected, testee.toString());
	}
	
	@Test
	void testCorrectCarIndex() {
		assertEquals(0, testee.getCarIndex());
		testee.incrementCarIndex();
		assertEquals(1, testee.getCarIndex());
		testee.incrementCarIndex();
		assertEquals(0, testee.getCarIndex());
	}
}
