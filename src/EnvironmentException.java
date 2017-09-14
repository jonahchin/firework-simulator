/**
 * An exception used by the Environment class.
 * @author Jonah Chin
 * @version 1.0
 */
public class EnvironmentException extends Exception {

	/**
	 * Accepts a specific message about the problem.
	 * @param message A string error message.
	 */
	public EnvironmentException (String message ) {
		super(message);
	}

} // end EnvironmentException class
