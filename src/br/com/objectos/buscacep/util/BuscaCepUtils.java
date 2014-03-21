package br.com.objectos.buscacep.util;

import java.util.Map;
import java.util.Map.Entry;

public class BuscaCepUtils {

	public static String getUrlConstruida(String url, Map<String, String> parametros, String... params) {
		StringBuilder sb = new StringBuilder();

		for (Entry<String, String> entry : parametros.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
		}

		for (int i = 0; i < params.length; i++) {
			if (sb.length() > 0 && i % 2 == 0) {
				sb.append("&");
			}
			sb.append(params[i]);
			if (i % 2 == 0) {
				sb.append("=");
			}
		}

		sb.insert(0, url + "?");

		return sb.toString();
	}

	public static String getInterno(String tag) {
		return tag.substring(tag.indexOf(">") + 1, tag.lastIndexOf("<"));
	}

}
