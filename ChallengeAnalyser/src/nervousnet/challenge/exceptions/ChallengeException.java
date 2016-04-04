package nervousnet.challenge.exceptions;

@SuppressWarnings("serial")
public class ChallengeException extends Exception {
	
	public String msg = null;
	
	public ChallengeException() {
		super();
	}
	
	public ChallengeException(String msg) {
		super(msg);
		this.msg = msg;		
	}

}
