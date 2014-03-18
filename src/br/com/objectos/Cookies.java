package br.com.objectos;

import java.net.HttpURLConnection;
import java.util.List;

public class Cookies {

	private final List<String> cookies;

	public Cookies(HttpURLConnection connection) {
		this(connection.getHeaderFields().get("Set-Cookie"));
	}

	protected Cookies(List<String> cookies) {
		this.cookies = cookies;
	}

	public void addCookiesToConnection(HttpURLConnection connection) throws BuscaCepException {
		if (!(isValid())) {
			throw new BuscaCepException("Sessão expirada.");
		}

		for (String cookie : cookies) {
			connection.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
		}
	}

	protected boolean isValid() {
		return cookies != null && !(cookies.isEmpty());
	}

}
