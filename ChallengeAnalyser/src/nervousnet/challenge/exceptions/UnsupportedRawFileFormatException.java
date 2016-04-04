package nervousnet.challenge.exceptions;

/**
 * Exception occurring when raw-value file naming convention
 * is corrupted. 
 * @author jovan
 *
 */
@SuppressWarnings("serial")
public class UnsupportedRawFileFormatException extends ChallengeException{
	
	public UnsupportedRawFileFormatException(String msg) {
		super(msg);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("nervousnet.challenge.exceptions.UnsupportedFileFormatException: ");
		sb.append(msg);
		return sb.toString();
	}

}
