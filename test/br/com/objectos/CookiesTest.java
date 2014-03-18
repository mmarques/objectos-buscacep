package br.com.objectos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CookiesTest {

	private URL url;
	private HttpURLConnection connection;
	private Cookies cookies;

	@Before
	public void before() throws Exception {
		url = new URL("https://github.com");
		connection = (HttpURLConnection) url.openConnection();
	}

	@After
	public void after() {
		connection.disconnect();
	}

	@Test
	public void testCreateCookiesFromConnection() throws Exception {
		cookies = new Cookies(connection);

		assertTrue(cookies.isValid());
	}

	@Test
	public void testAddCookiesToConnection() throws Exception {
		cookies = new Cookies(Arrays.asList("cookie"));

		cookies.addCookiesToConnection(connection);

		assertEquals("cookie", connection.getRequestProperty("Cookie"));
	}

	@Test
	public void testCookiesNull() {
		cookies = new Cookies((List<String>) null);

		assertFalse(cookies.isValid());
	}

	@Test
	public void testCookiesEmpty() {
		cookies = new Cookies(new ArrayList<String>());

		assertFalse(cookies.isValid());
	}

}
