package se233.audioconverterproject.model.exception;

import java.io.IOException;
import java.io.UncheckedIOException;

public class InvalidFileFormatException extends UncheckedIOException{
	public InvalidFileFormatException(String errMessage, IOException err) {
		super(errMessage, err);
	}
}