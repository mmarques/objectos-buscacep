package br.com.objectos;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class BuscaCepUtilsTest {

	@Test
	public void testGetUrlConstruida() {
		String url = "http://www.objectos.com.br";
		Map<String, String> parametros = new LinkedHashMap<String, String>() {
			private static final long serialVersionUID = -5922907136410047333L;

			{
				put("parametro1", "1");
				put("parametro2", "2");
				put("parametro3", "3");
			}
		};

		String urlConstruida = BuscaCepUtils.getUrlConstruida(url, parametros, "param1", "1", "param2", "2");

		assertEquals("http://www.objectos.com.br?parametro1=1&parametro2=2&parametro3=3&param1=1&param2=2", urlConstruida);
	}

	@Test
	public void testGetUrlConstruidaSemParametros() {
		String url = "http://www.objectos.com.br";
		Map<String, String> parametros = new LinkedHashMap<String, String>();
		String urlConstruida = BuscaCepUtils.getUrlConstruida(url, parametros, "param1", "1", "param2", "2");

		assertEquals("http://www.objectos.com.br?param1=1&param2=2", urlConstruida);
	}

	@Test
	public void testGetUrlConstruidaSemParams() {
		String url = "http://www.objectos.com.br";
		Map<String, String> parametros = new LinkedHashMap<String, String>() {
			private static final long serialVersionUID = -5922907136410047333L;

			{
				put("parametro1", "1");
				put("parametro2", "2");
				put("parametro3", "3");
			}
		};

		String urlConstruida = BuscaCepUtils.getUrlConstruida(url, parametros);

		assertEquals("http://www.objectos.com.br?parametro1=1&parametro2=2&parametro3=3", urlConstruida);
	}

	@Test
	public void testGetUrlConstruidaComParamsIncompleto() {
		String url = "http://www.objectos.com.br";
		Map<String, String> parametros = new LinkedHashMap<String, String>() {
			private static final long serialVersionUID = -5922907136410047333L;

			{
				put("parametro1", "1");
				put("parametro2", "2");
				put("parametro3", "3");
			}
		};

		String urlConstruida = BuscaCepUtils.getUrlConstruida(url, parametros, "param1", "1", "param2");

		assertEquals("http://www.objectos.com.br?parametro1=1&parametro2=2&parametro3=3&param1=1&param2=", urlConstruida);
	}

	@Test
	public void testGetInterno() {
		String tag = "<td class=\"labela\">Logradouro:</td>";

		String interno = BuscaCepUtils.getInterno(tag);

		assertEquals("Logradouro:", interno);
	}

	@Test
	public void testGetInternoComTags() {
		StringBuilder sb = new StringBuilder();
		sb.append("<td class=\"label\">Localidade / UF:</td>");
		sb.append("<td class=\"value\">São Paulo/SP</td>");

		String expected = sb.toString();

		sb.insert(0, "<tr>");
		sb.append("</tr>");
		String tag = sb.toString();

		String interno = BuscaCepUtils.getInterno(tag);

		assertEquals(expected, interno);
	}

}
