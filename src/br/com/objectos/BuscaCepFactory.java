package br.com.objectos;

public class BuscaCepFactory {

	private static BuscaCepFactory instance;

	private BuscaCepFactory() {

	}

	/**
	 * Recupera a instância da fábrica de BuscaCep.
	 * 
	 * @return BuscaCepFactory
	 */
	public static BuscaCepFactory instance() {
		if (instance == null) {
			instance = new BuscaCepFactory();
		}

		return instance;
	}

	/**
	 * Cria uma nova BuscaCep.
	 * 
	 * @return BuscaCep
	 */
	public BuscaCep newBuscaCep() {
		return new BuscaCepImpl();
	}

}
