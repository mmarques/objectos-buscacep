package br.com.objectos.buscacep;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.com.objectos.buscacep.BuscaCep;
import br.com.objectos.buscacep.BuscaCepException;
import br.com.objectos.buscacep.BuscaCepFactory;
import br.com.objectos.buscacep.bo.Endereco;

public class BuscaCepTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private BuscaCep buscaCep;

	@Before
	public void before() {
		buscaCep = BuscaCepFactory.instance().newBuscaCep();
	}

	@Test
	public void testBuscaCep() throws Exception {
		String cep = "05414-001";
		Endereco endereco = buscaCep.busca(cep);

		assertEquals("Rua Cônego Eugênio Leite - de 734 ao fim - lado par", endereco.getLogradouro());
		assertEquals("Cerqueira César", endereco.getBairro());
		assertEquals("São Paulo/SP", endereco.getLocalidade());
		assertEquals(cep, endereco.getCep());
	}

	@Test
	public void testBuscaCepAlternativo() throws Exception {
		String cep = "05414001";
		String cepFormatado = "05414-001";
		Endereco endereco = buscaCep.busca(cep);

		assertEquals("Rua Cônego Eugênio Leite - de 734 ao fim - lado par", endereco.getLogradouro());
		assertEquals("Cerqueira César", endereco.getBairro());
		assertEquals("São Paulo/SP", endereco.getLocalidade());
		assertEquals(cepFormatado, endereco.getCep());
	}

	@Test
	public void testBuscaCepDiferente() throws Exception {
		String cep = "04614-013";
		Endereco endereco = buscaCep.busca(cep);

		assertEquals("Rua Demóstenes - de 531/532 a 779/780", endereco.getLogradouro());
		assertEquals("Campo Belo", endereco.getBairro());
		assertEquals("São Paulo/SP", endereco.getLocalidade());
		assertEquals(cep, endereco.getCep());
	}

	@Test
	public void testBuscaCepInexistente() throws Exception {
		thrown.expect(BuscaCepException.class);
		thrown.expectMessage("O endereço informado");
		thrown.expectMessage("não foi encontrado.");

		String cep = "05414-009";
		buscaCep.busca(cep);
	}

	@Test
	public void testBuscaCepIncompleto() throws Exception {
		thrown.expect(BuscaCepException.class);
		thrown.expectMessage("O CEP informado deve estar no formato 99999-999 ou 99999999.");

		String cep = "05414-00";
		buscaCep.busca(cep);
	}

	@Test
	public void testBuscaCepInvalido() throws Exception {
		thrown.expect(BuscaCepException.class);
		thrown.expectMessage("O CEP informado deve estar no formato 99999-999 ou 99999999.");

		String cep = "05414-0000";
		buscaCep.busca(cep);
	}

}
