/**
 * Represents an exception specific to the Nyeash application.
 */
public class NyeashException extends Exception {

    /**
     * Creates a Nyeash exception with the given error message.
     *
     * @param message Error message.
     */
    public NyeashException(String message) {
        super(message);
    }
}