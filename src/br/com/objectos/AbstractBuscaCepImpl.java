package br.com.objectos;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

	protected enum CamposEndereco {
		
		LOGRADOURO("Logradouro:"),
		BAIRRO("Bairro:"),
		LOCALIDADE("Localidade / UF:"),
		CEP("CEP:");

		private CamposEndereco(String rotulo) {
			this.rotulo = rotulo;
		}

		private final String rotulo;

		public String getRotulo() {
			return rotulo;
		}

	}

	@Override
	public Endereco busca(String cep) throws BuscaCepException {
		if (!(cep.matches("[0-9]{5}-?[0-9]{3}"))) {
			throw new BuscaCepException("O CEP informado deve estar no formato 99999-999 ou 99999999.");
		}

		List<String> cookies = getCookiesConsulta(cep);

		return getDetalhe(cookies);
	}

	abstract protected List<String> getCookiesConsulta(String cep) throws BuscaCepException;

	abstract protected Endereco getDetalhe(List<String> cookies) throws BuscaCepException;

}
