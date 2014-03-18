package br.com.objectos;

import static br.com.objectos.EnderecoHelper.CamposEndereco.BAIRRO;
import static br.com.objectos.EnderecoHelper.CamposEndereco.CEP;
import static br.com.objectos.EnderecoHelper.CamposEndereco.LOCALIDADE;
import static br.com.objectos.EnderecoHelper.CamposEndereco.LOGRADOURO;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EnderecoHelperTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private EnderecoHelper enderecoHelper;

	@Before
	public void before() {
		enderecoHelper = new EnderecoHelper();
	}

	@Test
	public void testCreateEndereco() throws Exception {
		enderecoHelper.put(LOGRADOURO.getRotulo(), "logradouro");
		enderecoHelper.put(BAIRRO.getRotulo(), "bairro");
		enderecoHelper.put(LOCALIDADE.getRotulo(), "localidade");
		enderecoHelper.put(CEP.getRotulo(), "cep");
		
		Endereco endereco = enderecoHelper.createEndereco();
		
		assertEquals("logradouro", endereco.getLogradouro());
		assertEquals("bairro", endereco.getBairro());
		assertEquals("localidade", endereco.getLocalidade());
		assertEquals("cep", endereco.getCep());
	}

	@Test
	public void testCreateEnderecoInvalido() throws Exception {
		thrown.expect(BuscaCepException.class);
		thrown.expectMessage("Não foi possível recuperar o endereço desejado.");

		enderecoHelper.createEndereco();
	}

}
