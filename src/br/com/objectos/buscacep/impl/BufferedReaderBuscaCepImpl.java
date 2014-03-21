package br.com.objectos.buscacep.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.objectos.buscacep.BuscaCep;
import br.com.objectos.buscacep.BuscaCepException;
import br.com.objectos.buscacep.bo.Cookies;
import br.com.objectos.buscacep.bo.Endereco;
import br.com.objectos.buscacep.helper.EnderecoHelper;
import br.com.objectos.buscacep.helper.EnderecoHelper.CamposEndereco;
import br.com.objectos.buscacep.util.BuscaCepUtils;

public class BufferedReaderBuscaCepImpl extends AbstractBuscaCepImpl implements BuscaCep {

	private static final String ENDERECO_KEY_TAG = "<td class=\"label\">";

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
