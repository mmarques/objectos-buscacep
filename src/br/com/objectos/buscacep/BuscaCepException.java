package br.com.objectos.buscacep;

public class BuscaCepException extends Exception {

	private static final long serialVersionUID = 8951757237863055129L;

	public BuscaCepException() {
		super();
	}

	public BuscaCepException(String message) {
		super(message);
	}

	public BuscaCepException(String message, Throwable cause) {
		super(message, cause);
	}

}
