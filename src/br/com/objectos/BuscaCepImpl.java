package br.com.objectos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.swing.text.html.HTMLEditorKit;

public class BuscaCepImpl extends AbstractBuscaCepImpl implements BuscaCep {

	protected BuscaCepImpl() {

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

			HTMLEditorKit.Parser parse = new HTMLParse().getParser();
			ConsultaErroParser parser = new ConsultaErroParser(cep);
			parse.parse(reader, parser, true);

			if (parser.isErro()) {
				throw new BuscaCepException(parser.getErro());
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

			reader = new BufferedReader(new InputStreamReader(is, ISO_8859_1));

			HTMLEditorKit.Parser parse = new HTMLParse().getParser();
			DetalheParser parser = new DetalheParser();
			parse.parse(reader, parser, true);

			String logradouro = parser.getEnderecoMap().get(CamposEndereco.LOGRADOURO.getRotulo());
			String bairro = parser.getEnderecoMap().get(CamposEndereco.BAIRRO.getRotulo());
			String localidade = parser.getEnderecoMap().get(CamposEndereco.LOCALIDADE.getRotulo());
			String cepEndereco = parser.getEnderecoMap().get(CamposEndereco.CEP.getRotulo());

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
