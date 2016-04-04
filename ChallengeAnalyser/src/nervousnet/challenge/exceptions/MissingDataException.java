package nervousnet.challenge.exceptions;

/**
 * Exception occurring when data is not complete, both during
 * loading raw files into memory or dumping summarized values
 * to disk.
 * @author jovan
 *
 */
@SuppressWarnings("serial")
public class MissingDataException extends ChallengeException {
	
	public MissingDataException(String msg) {
		super(msg);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("nervousnet.challenge.exceptions.MissingDataException: ");
		sb.append(msg);
		return sb.toString();
	}

}
