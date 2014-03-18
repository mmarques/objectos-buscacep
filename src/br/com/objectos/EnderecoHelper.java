package br.com.objectos;

import java.util.HashMap;
import java.util.Map;

public class EnderecoHelper {

	private final Map<String, String> enderecoMap = new HashMap<String, String>();

	protected enum CamposEndereco {

		LOGRADOURO("Logradouro:"), BAIRRO("Bairro:"), LOCALIDADE("Localidade / UF:"), CEP("CEP:");

		private CamposEndereco(String rotulo) {
			this.rotulo = rotulo;
		}

		private final String rotulo;

		public String getRotulo() {
			return rotulo;
		}

	}

	public String put(String key, String value) {
		return enderecoMap.put(key, value);
	}

	public int size() {
		return enderecoMap.size();
	}

	public Endereco createEndereco() throws BuscaCepException {
		String logradouro = enderecoMap.get(CamposEndereco.LOGRADOURO.getRotulo());
		String bairro = enderecoMap.get(CamposEndereco.BAIRRO.getRotulo());
		String localidade = enderecoMap.get(CamposEndereco.LOCALIDADE.getRotulo());
		String cepEndereco = enderecoMap.get(CamposEndereco.CEP.getRotulo());

		if (logradouro == null || bairro == null || localidade == null || cepEndereco == null) {
			throw new BuscaCepException("Não foi possível recuperar o endereço desejado.");
		}

		return new Endereco(logradouro, bairro, localidade, cepEndereco);
	}

}
