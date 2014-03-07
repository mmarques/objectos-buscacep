package br.com.objectos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BuscaCepImplTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private BuscaCepImpl buscaCep;

	@Before
	public void before() {
		buscaCep = new BuscaCepImpl();
	}

	@Test
	public void testGetCookiesConsulta() throws Exception {
		String cep = "05414-001";
		List<String> cookies = buscaCep.getCookiesConsulta(cep);

		assertFalse(cookies.isEmpty());
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
		List<String> cookies = buscaCep.getCookiesConsulta(cep);

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

		buscaCep.getDetalhe(null);
	}

	@Test
	public void testGetDetalheCookiesVazio() throws Exception {
		thrown.expect(BuscaCepException.class);
		thrown.expectMessage("Sessão expirada.");

		buscaCep.getDetalhe(new ArrayList<String>());
	}

	@Test
	public void testGetDetalheCookiesCorrespondentes() throws Exception {
		String cep = "05414-001";
		String cep2 = "04614-013";

		List<String> cookies = buscaCep.getCookiesConsulta(cep);
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
		List<String> cookies = buscaCep.getCookiesConsulta(cep2);

		Endereco endereco = buscaCep.getDetalhe(cookies);

		assertNotEquals("Rua Cônego Eugênio Leite - de 734 ao fim - lado par", endereco.getLogradouro());
		assertNotEquals("Cerqueira César", endereco.getBairro());
		assertNotEquals(cep, endereco.getCep());
	}

}
