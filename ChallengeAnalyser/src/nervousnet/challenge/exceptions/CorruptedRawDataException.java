package nervousnet.challenge.exceptions;

/**
 * Exception occurring when:
 * 	- out of range day ID appears in raw-value files
 * 	- out of range time ID appears in raw-value files
 * 	- cannot parse correctly line from raw files (when letters appear, or not enough arguments in a line...)
 * @author jovan
 *
 */
@SuppressWarnings("serial")
public class CorruptedRawDataException extends ChallengeException {
	
	public CorruptedRawDataException(String msg) {
		super(msg);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("nervousnet.challenge.exceptions.CorruptedRawDataException: ");
		sb.append(msg);
		return sb.toString();
	}

}
