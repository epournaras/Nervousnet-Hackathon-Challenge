package nervousnet.challenge.exceptions;

/**
 * Error occurring when a whole raw-value user file is missing.
 * @author jovan
 *
 */
@SuppressWarnings("serial")
public class MissingFileException extends ChallengeException {
	
	public MissingFileException(String msg) {
		super(msg);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("nervousnet.challenge.exceptions.MissingFileException: ");
		sb.append(msg);
		return sb.toString();
	}

}
