package org.openlca.olcatdb.conversion;

public class ConversionException extends Exception {

	private static final long serialVersionUID = 8456423936272636676L;

	public ConversionException() {		
	}

	public ConversionException(String message) {
		super(message);		
	}

	public ConversionException(Throwable cause) {
		super(cause);		
	}

	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}

}
