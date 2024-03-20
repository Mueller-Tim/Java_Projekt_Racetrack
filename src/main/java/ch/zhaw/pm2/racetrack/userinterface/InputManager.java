package ch.zhaw.pm2.racetrack.userinterface;

import ch.zhaw.pm2.racetrack.strategy.*;
import model.Direction;

import org.beryx.textio.TextIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * The InputManager class is responsible for getting and validating user input
 */
public class InputManager {

    /**
     * Get the file from TextIO as user input
     *
     * @param textIO
     * @param moveList
     * @param directory
     * @param requestType
     * @return File
     * @throws FileNotFoundException
     */
    public File getFileInput(TextIO textIO, List<String> fileNameList, File directory, RequestType requestType) throws FileNotFoundException {
        int result = textIO.newIntInputReader().withMaxVal(fileNameList.size()).withMinVal(1).read(buildListRequest(fileNameList, requestType));
        String filename = fileNameList.get(result - 1);
        return new File(directory.getName() + "/" + filename);
    }

    /**
     * Get the Move Strategy from TextIO as user input {@link ch.zhaw.pm2.racetrack.strategy.MoveStrategy.StrategyType}
     * In a switch case there will be returned a new object of the movestrategy
     *
     * @param textIO
     * @param promptMessage
     * @return {@link MoveStrategy} for default throw exception
     */
    public MoveStrategy.StrategyType getStrategyInput(TextIO textIO, String promptMessage) {
        MoveStrategy.StrategyType strategyType = textIO.newEnumInputReader(MoveStrategy.StrategyType.class).read(promptMessage);
        return strategyType;
    }

    /**
     * Build a String with all possible arguments for {@link InputManager.RequestType}
     *
     * @param list
     * @param requestType
     * @return string with all arguments
     */
    public String buildListRequest(List<String> list, RequestType requestType) {
        String request = requestType.toString();
        for (int i = 0; i < list.size(); i++) {
            request += i + 1 + ": " + list.get(i) + "\n";
        }
        request += "input: ";
        return request;
    }

    public enum RequestType {
        MOVELIST("Select a move list:\n"),
        RACETRACK("Select a track:\n"),
        PATH_FOLLOWER("Select a path follower file:\n");

        private String message;

        RequestType(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return this.message;
        }
    }
}
