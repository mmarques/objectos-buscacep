package br.com.objectos.buscacep.impl;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.objectos.buscacep.BuscaCep;
import br.com.objectos.buscacep.BuscaCepException;
import br.com.objectos.buscacep.bo.Cookies;
import br.com.objectos.buscacep.bo.Endereco;

public abstract class AbstractBuscaCepImpl implements BuscaCep {

	protected static final String BUSCA_URL = "http://www.buscacep.correios.com.br/servicos/dnec/consultaLogradouroAction.do";
	protected static final Map<String, String> BUSCA_URL_PARAMETROS = new LinkedHashMap<String, String>() {
		private static final long serialVersionUID = 657831577064459797L;

		{
			put("Metodo", "listaLogradouro");
			put("TipoConsulta", "relaxation");
			put("StartRow", "1");
			put("EndRow", "10");
		}
	};

	protected static final String DETALHES_URL = "http://www.buscacep.correios.com.br/servicos/dnec/detalheCEPAction.do";
	protected static final Map<String, String> DETALHES_URL_PARAMETROS = new LinkedHashMap<String, String>() {
		private static final long serialVersionUID = -7738951599841065132L;

		{
			put("Metodo", "detalhe");
			put("Posicao", "1");
			put("TipoCep", "2");
			put("CEP", "");
		}
	};

	protected static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

	protected static final String ERRO_FORMATO = "O endereço informado %s não foi encontrado.";

	@Override
	public Endereco busca(String cep) throws BuscaCepException {
		if (!(cep.matches("[0-9]{5}-?[0-9]{3}"))) {
			throw new BuscaCepException("O CEP informado deve estar no formato 99999-999 ou 99999999.");
		}

		Cookies cookies = getCookiesConsulta(cep);

		return getDetalhe(cookies);
	}

	protected abstract Cookies getCookiesConsulta(String cep) throws BuscaCepException;

	protected abstract Endereco getDetalhe(Cookies cookies) throws BuscaCepException;

}
