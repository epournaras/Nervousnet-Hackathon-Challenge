package nervousnet.challenge.exceptions;

/**
 * Exception occurring when illegal user, day or time ID is used
 * as a query to a Loader's hash map.
 * @author jovan
 *
 */
@SuppressWarnings("serial")
public class IllegalHashMapArgumentException extends ChallengeException {
	
	public IllegalHashMapArgumentException(String msg) {
		super(msg);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("nervousnet.challenge.exceptions.IllegalHashMapArgument: ");
		sb.append(msg);
		return sb.toString();
	}

}
