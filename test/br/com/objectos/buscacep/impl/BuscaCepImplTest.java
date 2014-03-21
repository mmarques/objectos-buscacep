package br.com.objectos.buscacep.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.com.objectos.buscacep.BuscaCepException;
import br.com.objectos.buscacep.bo.Cookies;
import br.com.objectos.buscacep.bo.Endereco;

public class BuscaCepImplTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private HTMLParseBuscaCepImpl buscaCep;

	@Before
	public void before() {
		buscaCep = new HTMLParseBuscaCepImpl();
	}

	@Test
	public void testGetCookiesConsulta() throws Exception {
		String cep = "05414-001";
		Cookies cookies = buscaCep.getCookiesConsulta(cep);

		assertTrue(cookies.isValid());
	}

	@Test
	public void testGetCookiesConsultaInexistente() throws Exception {
		thrown.expect(BuscaCepException.class);
		thrown.expectMessage("O endereço informado");
		thrown.expectMessage("não foi encontrado.");

		String cep = "05414-009";
		buscaCep.getCookiesConsulta(cep);
	}

	@Test
	public void testGetDetalhe() throws Exception {
		String cep = "05414-001";
		Cookies cookies = buscaCep.getCookiesConsulta(cep);

		Endereco endereco = buscaCep.getDetalhe(cookies);

		assertEquals("Rua Cônego Eugênio Leite - de 734 ao fim - lado par", endereco.getLogradouro());
		assertEquals("Cerqueira César", endereco.getBairro());
		assertEquals("São Paulo/SP", endereco.getLocalidade());
		assertEquals(cep, endereco.getCep());
	}

	@Test
	public void testGetDetalheSemCookies() throws Exception {
		thrown.expect(BuscaCepException.class);
		thrown.expectMessage("Sessão expirada.");

		buscaCep.getDetalhe(new Cookies((List<String>) null) {
			// inner type
		});
	}

	@Test
	public void testGetDetalheCookiesVazio() throws Exception {
		thrown.expect(BuscaCepException.class);
		thrown.expectMessage("Sessão expirada.");

		buscaCep.getDetalhe(new Cookies(new ArrayList<String>()) {
			// inner type
		});
	}

	@Test
	public void testGetDetalheCookiesCorrespondentes() throws Exception {
		String cep = "05414-001";
		String cep2 = "04614-013";

		Cookies cookies = buscaCep.getCookiesConsulta(cep);
		buscaCep.getCookiesConsulta(cep2);

		Endereco endereco = buscaCep.getDetalhe(cookies);

		assertEquals("Rua Cônego Eugênio Leite - de 734 ao fim - lado par", endereco.getLogradouro());
		assertEquals("Cerqueira César", endereco.getBairro());
		assertEquals("São Paulo/SP", endereco.getLocalidade());
		assertEquals(cep, endereco.getCep());
	}

	@Test
	public void testGetDetalheCookiesNaoCorrespondentes() throws Exception {
		String cep = "05414-001";
		String cep2 = "04614-013";

		buscaCep.getCookiesConsulta(cep);
		Cookies cookies = buscaCep.getCookiesConsulta(cep2);

		Endereco endereco = buscaCep.getDetalhe(cookies);

		assertNotEquals("Rua Cônego Eugênio Leite - de 734 ao fim - lado par", endereco.getLogradouro());
		assertNotEquals("Cerqueira César", endereco.getBairro());
		assertNotEquals(cep, endereco.getCep());
	}

}
