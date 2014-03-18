package br.com.objectos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.objectos.EnderecoHelper.CamposEndereco;

public class BuscaCepImpl2 extends AbstractBuscaCepImpl implements BuscaCep {

	private static final String ENDERECO_KEY_TAG = "<td class=\"label\">";

	protected BuscaCepImpl2() {

	}

	@Override
	protected Cookies getCookiesConsulta(String cep) throws BuscaCepException {
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

			Cookies cookies = new Cookies(connection);

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
	protected Endereco getDetalhe(Cookies cookies) throws BuscaCepException {
		BufferedReader reader = null;
		HttpURLConnection connection = null;

		try {

			URL url = new URL(BuscaCepUtils.getUrlConstruida(DETALHES_URL, DETALHES_URL_PARAMETROS));
			connection = (HttpURLConnection) url.openConnection();

			cookies.addCookiesToConnection(connection);

			InputStream is = connection.getInputStream();

			EnderecoHelper enderecoHelper = new EnderecoHelper();

			reader = new BufferedReader(new InputStreamReader(is, ISO_8859_1));
			String line;
			String nextLine;
			while ((line = reader.readLine()) != null) {
				if (line.contains(ENDERECO_KEY_TAG)) {
					nextLine = reader.readLine();
					enderecoHelper.put(BuscaCepUtils.getInterno(line), BuscaCepUtils.getInterno(nextLine));
				}

				if (enderecoHelper.size() == CamposEndereco.values().length) {
					break;
				}
			}

			return enderecoHelper.createEndereco();

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
