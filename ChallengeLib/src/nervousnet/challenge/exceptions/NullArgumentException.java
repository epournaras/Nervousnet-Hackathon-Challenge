package nervousnet.challenge.exceptions;

/**
 * Exception occurring when null pointer is used as a query to Loader's hash map.
 * @author jovan
 *
 */
@SuppressWarnings("serial")
public class NullArgumentException extends ChallengeException {
	
	public NullArgumentException(String msg) {
		super(msg);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("nervousnet.challenge.exceptions.NullArgumentException: ");
		sb.append(msg);
		return sb.toString();
	}

}
