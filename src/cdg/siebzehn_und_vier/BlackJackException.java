package cdg.siebzehn_und_vier;

public class BlackJackException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BlackJackException() {

	}

	public BlackJackException( String message ) {
		super( message );
	}

	public BlackJackException( Throwable cause ) {
		super( cause );
	}

	public BlackJackException( String message, Throwable cause ) {
		super( message, cause );
	}

	public BlackJackException( String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace ) {
		super( message, cause, enableSuppression, writableStackTrace );
	}

}
