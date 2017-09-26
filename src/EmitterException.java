


/**
 * Thrown by the Emitter object if an illegal angle is supplied.
 * @author Jonah Chin
 * @version 1.0
 */
@SuppressWarnings("serial")
public class EmitterException extends Exception {

	/**
	 * Accepts a specific message about the problem.
	 * @param message A string error message.
	 */
	public EmitterException(String message) {
		super(message);
	}

} // end EmitterException class
