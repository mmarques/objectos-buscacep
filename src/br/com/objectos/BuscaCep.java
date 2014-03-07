package br.com.objectos;

public interface BuscaCep {

	/**
	 * Busca o endereço que corresponde exatamente ao cep informado.
	 * 
	 * @param cep
	 * @return Endereco
	 * @throws BuscaCepException
	 *             caso o cep não seja correspondente a um endereço.
	 */
	public Endereco busca(String cep) throws BuscaCepException;

}
