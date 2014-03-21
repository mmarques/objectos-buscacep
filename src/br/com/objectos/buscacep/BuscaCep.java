package br.com.objectos.buscacep;

import br.com.objectos.buscacep.bo.Endereco;

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
