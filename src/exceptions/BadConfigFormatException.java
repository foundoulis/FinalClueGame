package exceptions;

@SuppressWarnings("serial")
public class BadConfigFormatException extends Exception {
	public BadConfigFormatException() {
	}

	public BadConfigFormatException(String x) {
		super(x);
	}
}
