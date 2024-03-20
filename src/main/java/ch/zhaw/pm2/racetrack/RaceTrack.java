package ch.zhaw.pm2.racetrack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.zhaw.pm2.racetrack.strategy.*;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import ch.zhaw.pm2.racetrack.Config.OutputMessage;
import ch.zhaw.pm2.racetrack.userinterface.InputManager;
import ch.zhaw.pm2.racetrack.userinterface.OutputWriter;
import exceptions.InvalidFileFormatException;
import model.PositionVector;

/**
 * The RaceTrack class is responsible for managing the game procedure and also
 * serves as the entry point It initializes the necessary objects of the
 * OutputManager and InputManager classes and creates a new Game with a specific
 * track file. The UI of the Racetrack game works with the Beryx library TextIO
 * which provides essential functions for input and output.
 */
public class RaceTrack {
	private static final TextIO textIO = TextIoFactory.getTextIO();
	private static final TextTerminal<?> textTerminal = textIO.getTextTerminal();
	private final OutputWriter outputWriter = new OutputWriter();
	private final InputManager inputManager = new InputManager();
	private Game game;
	private Config config = new Config();

	/**
	 * Game entry point
	 * @param args
	 */
	public static void main(String[] args) {
		new RaceTrack().setup();
	}

	private void setup() {
		outputWriter.print(textTerminal, Config.OutputMessage.GREETING.toString());
		setTrack();
		setMoveStrategyToEachCar();
		race();
	}

	private List<String> getFileNames(File namesDirectory) {
		List<String> fileNames = new ArrayList<>();
		File[] pathnames = namesDirectory.listFiles();
		for (int i = 0; i < pathnames.length; i++) {
			if (pathnames[i].isFile()) {
				fileNames.add(pathnames[i].getName());
			}
		}
		return fileNames;
	}

	private void setTrack() {
		try {
			Track track = new Track(inputManager.getFileInput(textIO, getFileNames(config.getTrackDirectory()),
					config.getTrackDirectory(), InputManager.RequestType.RACETRACK));
			this.game = new Game(track);
		} catch (IOException | InvalidFileFormatException e) {
			e.printStackTrace();
		}
	}

	private void setMoveStrategyToEachCar() {
		for (int i = 0; i < game.getCarCount(); i++) {
			game.setCarMoveStrategy(i, fetchMoveStrategyInput(game.getCarId(i), game.getCarPosition(i)));
		}
	}

	private MoveStrategy fetchMoveStrategyInput(char carId, PositionVector carCurrentPosition) {
		try {
			switch (inputManager.getStrategyInput(textIO, OutputMessage.STRATEGY.toString() + carId)) {
				case USER:
					return new UserMoveStrategy(textIO, carId);
				case MOVE_LIST:
					return new MoveListStrategy(inputManager.getFileInput(textIO, getFileNames(config.getMoveDirectory()),
							config.getMoveDirectory(), InputManager.RequestType.MOVELIST));
				case DO_NOT_MOVE:
					return new DoNotMoveStrategy();
				case PATH_FOLLOWER:
					return new PathFollowerMoveStrategy(
							inputManager.getFileInput(textIO, getFileNames(config.getFollowerDirectory()), config.getFollowerDirectory(), InputManager.RequestType.PATH_FOLLOWER),
							carCurrentPosition);
				case PATH_FINDER:
					return new PathFinderMoveStrategy(carCurrentPosition, game.getTrack());
				default:
					return fetchMoveStrategyInput(carId, carCurrentPosition);
			}
		} catch (InvalidFileFormatException | FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void race() {
		boolean gameInPlay = true;
		outputWriter.print(textTerminal, game.getTrackString());
		while (game.getWinner() == Game.NO_WINNER && gameInPlay) {
			if (!game.checkForMovingCars()) {
				outputWriter.print(textTerminal, OutputMessage.NOMOVINGCAR.toString());
				gameInPlay = false;
			} else {
				game.doCarTurn(game.getMoveForCurrentCar());
				game.switchToNextActiveCar();
				outputWriter.print(textTerminal, game.getTrackString());
			}
		}
		if (game.getWinner() != Game.NO_WINNER) {
			outputWriter.print(textTerminal, OutputMessage.WINNING.toString(),
					new String[]{String.valueOf(game.getCarId(game.getWinner()))});
		}
	}
}
