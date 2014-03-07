package br.com.objectos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuscaCepImpl2 extends AbstractBuscaCepImpl implements BuscaCep {

	private static final String ENDERECO_KEY_TAG = "<td class=\"label\">";

	protected BuscaCepImpl2() {

	}

	@Override
	protected List<String> getCookiesConsulta(String cep) throws BuscaCepException {
		BufferedReader reader = null;
		HttpURLConnection connection = null;

		try {

			URL url = new URL(BuscaCepUtils.getUrlConstruida(BUSCA_URL, BUSCA_URL_PARAMETROS, BUSCA_URL_PARAMETROS.get("TipoConsulta"), cep));
			connection = (HttpURLConnection) url.openConnection();

			InputStream is = connection.getInputStream();

			reader = new BufferedReader(new InputStreamReader(is, ISO_8859_1));
			String line;
			String erroFormatado = String.format(ERRO_FORMATO, cep);
			while ((line = reader.readLine()) != null) {
				if (line.contains(erroFormatado)) {
					throw new BuscaCepException(erroFormatado);
				}

			}

			List<String> cookies = connection.getHeaderFields().get("Set-Cookie");

			return cookies;

		} catch (IOException e) {
			throw new BuscaCepException(e.getClass().getName(), e);

		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				throw new BuscaCepException(e.getClass().getName(), e);
			}

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	@Override
	protected Endereco getDetalhe(List<String> cookies) throws BuscaCepException {
		if (cookies == null || cookies.isEmpty()) {
			throw new BuscaCepException("Sessão expirada.");
		}

		BufferedReader reader = null;
		HttpURLConnection connection = null;

		try {

			URL url = new URL(BuscaCepUtils.getUrlConstruida(DETALHES_URL, DETALHES_URL_PARAMETROS));
			connection = (HttpURLConnection) url.openConnection();

			for (String cookie : cookies) {
				connection.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
			}

			InputStream is = connection.getInputStream();

			Map<String, String> enderecoMap = new HashMap<String, String>();

			reader = new BufferedReader(new InputStreamReader(is, ISO_8859_1));
			String line;
			String nextLine;
			while ((line = reader.readLine()) != null) {
				if (line.contains(ENDERECO_KEY_TAG)) {
					nextLine = reader.readLine();
					enderecoMap.put(BuscaCepUtils.getInterno(line), BuscaCepUtils.getInterno(nextLine));
				}

				if (enderecoMap.size() == CamposEndereco.values().length) {
					break;
				}
			}

			String logradouro = enderecoMap.get(CamposEndereco.LOGRADOURO.getRotulo());
			String bairro = enderecoMap.get(CamposEndereco.BAIRRO.getRotulo());
			String localidade = enderecoMap.get(CamposEndereco.LOCALIDADE.getRotulo());
			String cepEndereco = enderecoMap.get(CamposEndereco.CEP.getRotulo());

			return new Endereco(logradouro, bairro, localidade, cepEndereco);

		} catch (IOException e) {
			throw new BuscaCepException(e.getClass().getName(), e);

		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				throw new BuscaCepException(e.getClass().getName(), e);
			}

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}
