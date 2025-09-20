package se233.audioconverterproject.model.exception;

public class ConversionFailedException extends RuntimeException {
	public ConversionFailedException(String errMessage) {
		super(errMessage);
	}
}