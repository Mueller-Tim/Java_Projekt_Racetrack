package ch.zhaw.pm2.racetrack.strategy;

import exceptions.InvalidFileFormatException;
import model.Direction;

import java.io.*;
import java.util.ArrayList;

/**
 * Determines the next move based on a file containing a list of directions.
 */
public class MoveListStrategy implements MoveStrategy {

    private ArrayList<Direction> movesList = new ArrayList<>();

    private int moveNumber = 0;

    /**
     Constructs a new MoveListStrategy instance by reading the move file.
     @param moveFile the file containing the list of moves.
     @throws FileNotFoundException if the move file cannot be found.
     @throws InvalidFileFormatException if the move file has an invalid format.
     */
    public MoveListStrategy(File moveFile) throws InvalidFileFormatException, FileNotFoundException {
        readFile(moveFile);
    }

    /**
     * {@inheritDoc}
     *
     * @return next direction from move file or NONE, if no more moves are available.
     */
    @Override
    public Direction nextMove() {
        Direction nextDirection;
        if(moveNumber < movesList.size()){
            nextDirection = movesList.get(moveNumber);
            moveNumber ++;
        } else{
            nextDirection = Direction.NONE;
        }
        return nextDirection;
    }

    /**
     Reads the move file and stores each move in the moves list.
     @param moveFile the file containing the list of moves.
     @throws FileNotFoundException if the move file cannot be found.
     @throws InvalidFileFormatException if the move file has an invalid format.
     @throws RuntimeException if an error occurs while reading the move file.
     */
    private void readFile(File moveFile) throws InvalidFileFormatException, FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(moveFile));
        try {
            String move;
            while ((move = reader.readLine()) != null) {
                if(!(move.isEmpty())){
                    movesList.add(checkMoveLine(move));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     Checks if a move line is valid and returns the corresponding Direction enum constant.
     @param moveLine a move line from the move file.
     @return the corresponding Direction enum constant.
     @throws InvalidFileFormatException if the move line is invalid.
     */
    private Direction checkMoveLine(String moveLine) throws InvalidFileFormatException {
        Direction direction;
        try{
            direction = Direction.valueOf(moveLine);
        } catch (IllegalArgumentException e){
            throw new InvalidFileFormatException("The Move-List file contains an illegal Movement: " + moveLine);
        }
        return direction;
    }

    public ArrayList<Direction> getMovesList(){
        return movesList;
    }
}
