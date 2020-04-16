package common.exceptions;

/**
 * Is throwed when command can't be used.
 */
public class CommandUsageException extends Exception {
    public CommandUsageException() {
        super();
    }

    /**
	 * Is throwed when command can't be used.
	 * @param message Message to throw.
 	*/
    public CommandUsageException(String message) {
        super(message);
    }
}
