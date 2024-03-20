package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.TrackSpecification;
import exceptions.InvalidFileFormatException;
import model.Car;
import model.PositionVector;
import model.SpaceType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the racetrack board.
 *
 * <p>
 * The racetrack board consists of a rectangular grid of 'width' columns and
 * 'height' rows. The zero point of he grid is at the top left. The x-axis
 * points to the right and the y-axis points downwards.
 * </p>
 * <p>
 * Positions on the track grid are specified using {@link PositionVector}
 * objects. These are vectors containing an x/y coordinate pair, pointing from
 * the zero-point (top-left) to the addressed space in the grid.
 * </p>
 *
 * <p>
 * Each position in the grid represents a space which can hold an enum object of
 * type {@link SpaceType}.<br>
 * Possible Space types are:
 * <ul>
 * <li>WALL : road boundary or off track space</li>
 * <li>TRACK: road or open track space</li>
 * <li>FINISH_LEFT, FINISH_RIGHT, FINISH_UP, FINISH_DOWN : finish line spaces
 * which have to be crossed in the indicated direction to winn the race.</li>
 * </ul>
 * <p>
 * Beside the board the track contains the list of cars, with their current
 * state (position, velocity, crashed,...)
 * </p>
 *
 * <p>
 * At initialization the track grid data is read from the given track file. The
 * track data must be a rectangular block of text. Empty lines at the start are
 * ignored. Processing stops at the first empty line following a non-empty line,
 * or at the end of the file.
 * </p>
 * <p>
 * Characters in the line represent SpaceTypes. The mapping of the Characters is
 * as follows:
 * </p>
 * <ul>
 * <li>WALL : '#'</li>
 * <li>TRACK: ' '</li>
 * <li>FINISH_LEFT : '&lt;'</li>
 * <li>FINISH_RIGHT: '&gt;'</li>
 * <li>FINISH_UP : '^;'</li>
 * <li>FINISH_DOWN: 'v'</li>
 * <li>Any other character indicates the starting position of a car.<br>
 * The character acts as the id for the car and must be unique.<br>
 * There are 1 to {@link TrackSpecification#MAX_CARS} allowed.</li>
 * </ul>
 *
 * <p>
 * All lines must have the same length, used to initialize the grid width).<br/>
 * Beginning empty lines are skipped. <br/>
 * The track ends with the first empty line or the file end.<br>
 * An {@link InvalidFileFormatException} is thrown, if
 * <ul>
 * <li>the file contains no track lines (grid height is 0)</li>
 * <li>not all track lines have the same length</li>
 * <li>the file contains no cars</li>
 * <li>the file contains more than {@link TrackSpecification#MAX_CARS} cars</li>
 * </ul>
 *
 * <p>
 * The Tracks {@link #toString()} method returns a String representing the
 * current state of the race (including car positions and status)
 * </p>
 */
public class Track implements TrackSpecification {

	private List<List<SpaceType>> fields = new ArrayList<>();
	private List<Car> cars = new ArrayList<>();
	private int carIndex = 0;

	/**
	 * Initialize a Track from the given track file.<br/>
	 * See class description for structure and valid tracks.
	 *
	 * @param trackFile Reference to a file containing the track data
	 * @throws IOException                if the track file can not be opened or
	 *                                    reaaing fails
	 * @throws InvalidFileFormatException if the track file contains invalid data
	 *                                    (no track lines, inconsistent length, no
	 *                                    cars)
	 */
	public Track(File trackFile) throws IOException, InvalidFileFormatException {
		populateField(trackFile);
		if(!(validateTrack(trackFile))){
			throw new InvalidFileFormatException("The track file contains invalid data");
		}
	}

	/**
	 * Returns the list of cars
	 * 
	 * @return List<Car> cars
	 */
	public List<Car> getCars() {
		return cars;
	}

	/**
	 * Returns the field
	 * 
	 * @return List<List<SpaceType>> fields
	 */
	public List<List<SpaceType>> getFields() {
		return fields;
	}


	private void populateField(File trackFile) throws FileNotFoundException {
		Scanner scanner = new Scanner(trackFile);
		int lineNumber = 0;
		while (scanner.hasNext()) {
			int columnNumber = 0;
			List<SpaceType> line = new ArrayList<>();
			for (char c : scanner.nextLine().toCharArray()) {
				line.add(mapCharToType(c, lineNumber, columnNumber));
				columnNumber++;
			}
			fields.add(line);
			lineNumber++;
		}
		scanner.close();
	}


	private SpaceType mapCharToType(char c, int lineNumber, int columnNumber) {
		if (SpaceType.spaceTypeForChar(c).isPresent()) {
			return SpaceType.spaceTypeForChar(c).get();
		}
		// c represents a car
		cars.add(new Car(c, new PositionVector(columnNumber, lineNumber)));
		return SpaceType.TRACK;
	}


	private boolean validateTrack(File trackFile) throws FileNotFoundException {
		if(validAmountCars(trackFile) && hasFinishLine(trackFile) && isRectangular(trackFile)){
			return true;
		} else {
			return false;
		}
	}



	private boolean isRectangular(File trackFile) throws FileNotFoundException {
		int columnNumber = 0;
		boolean isRectangular = true;
		for (List<SpaceType> spaceTypeList : getFields()){
			if(columnNumber == 0){
				columnNumber = spaceTypeList.size();
			} else if(columnNumber != spaceTypeList.size()){
				isRectangular = false;
			}

		}
		if(columnNumber == 0){
			isRectangular = false;
		}
		return isRectangular;
	}

	private boolean hasFinishLine(File trackFile) throws FileNotFoundException {
		boolean hasFinishLine = false;
		for (List<SpaceType> spaceTypeList : getFields()){
			for (SpaceType spaceType: spaceTypeList){
				if(SpaceType.FINISH_DOWN == spaceType || SpaceType.FINISH_UP == spaceType  ||
						SpaceType.FINISH_RIGHT == spaceType  || SpaceType.FINISH_LEFT == spaceType ){
					hasFinishLine = true;
				}
			}

		}
		return hasFinishLine;
	}


	private boolean validAmountCars(File trackFile) throws FileNotFoundException {
		int minCarAmount = 1;
		if(getCarCount() >= minCarAmount && getCarCount() <= MAX_CARS){
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Returns the current car index
	 * 
	 * @return int carIndex
	 */
	public int getCarIndex() {
		return carIndex;
	}

	/**
	 * Increments the current car index
	 */
	public void incrementCarIndex() {
		if (carIndex == getCarCount()-1) {
			carIndex = 0;
		} else {
			carIndex++;
		}
	}

	/**
	 * Return the height (number of rows) of the track grid.
	 * 
	 * @return Height of the track grid
	 */
	public int getHeight() {
		return fields.size();
	}

	/**
	 * Return the width (number of columns) of the track grid.
	 * 
	 * @return Width of the track grid
	 */
	public int getWidth() {
		return fields.get(0).size();
	}

	/**
	 * Return the number of cars.
	 *
	 * @return Number of cars
	 */
	@Override
	public int getCarCount() {
		return cars.size();
	}

	/**
	 * Get instance of specified car.
	 *
	 * @param carIndex The zero-based carIndex number
	 * @return The car instance at the given index
	 */
	@Override
	public Car getCar(int carIndex) {
		return cars.get(carIndex);
	}

	/**
	 * Return the type of space at the given position. If the location is outside
	 * the track bounds, it is considered a WALL.
	 *
	 * @param position The coordinates of the position to examine
	 * @return The type of track position at the given location
	 */
	@Override
	public SpaceType getSpaceTypeAtPosition(PositionVector position) {
		return fields.get(position.getY()).get(position.getX());
	}

	/**
	 * Gets the character representation for the given position of the racetrack,
	 * including cars.<br/>
	 * This can be used for generating the {@link #toString()} representation of the
	 * racetrack.<br/>
	 * If there is an active car (not crashed) at the given position, then the car
	 * id is returned.<br/>
	 * If there is a crashed car at the position, {@link #CRASH_INDICATOR} is
	 * returned.<br/>
	 * Otherwise, the space character for the given position is returned
	 *
	 * @param row row (y-value) of the racetrack position
	 * @param col column (x-value) of the racetrack position
	 * @return character representing the position (col,row) on the track or
	 *         {@link Car#getId()} resp. {@link #CRASH_INDICATOR}, if a car is at
	 *         the given position
	 */
	@Override
	public char getCharRepresentationAtPosition(int row, int col) {
		for (Car car : cars) {
			if (car.getCurrentPosition().getX() == col && car.getCurrentPosition().getY() == row) {
				if(car.isCrashed()){
					return Config.CRASH_INDICATOR;
				} else {
					return car.getId();
				}

			}
		}
		return getSpaceTypeAtPosition(new PositionVector(col, row)).getSpaceChar();
	}

	/**
	 * Return a String representation of the track, including the car locations and
	 * status.
	 * 
	 * @return A String representation of the track
	 */
	@Override
	public String toString() {
		String output = "";
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				output += getCharRepresentationAtPosition(i, j);
			}
			output += "\n";
		}
		return output;
	}
}
