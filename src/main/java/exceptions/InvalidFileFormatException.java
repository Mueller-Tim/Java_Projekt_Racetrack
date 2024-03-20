package exceptions;

/**
 * Used for invalid formatted Move-List and track files.
 */
public class InvalidFileFormatException extends Exception {

    /**
     * Constructs a new InvalidFileFormatException with the specified detail message.
     * @param message The detail message, which is saved for later retrieval by the getMessage() method.
     */

    public InvalidFileFormatException(String message) {
        super(message);
    }
}
