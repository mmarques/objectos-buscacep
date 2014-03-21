package br.com.objectos.buscacep.impl;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

public class ConsultaErroParser extends ParserCallback {

	private final String cep;

	private boolean verificaErroTitle;
	private boolean verificaErroFont;
	private boolean erroTitle;
	private boolean erroFont;
	private String erro;

	public ConsultaErroParser(String cep) {
		this.cep = cep;
	}

	@Override
	public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
		if (t == HTML.Tag.TITLE) {
			verificaErroTitle = true;
		}

		if (t == HTML.Tag.FONT) {
			verificaErroFont = true;
		}
	}

	@Override
	public void handleText(char[] data, int pos) {
		if (verificaErroTitle) {
			if (new String(data).equals("Erro")) {
				erroTitle = true;
				verificaErroTitle = false;
			}
		}

		if (verificaErroFont) {
			String erroFormatado = String.format(AbstractBuscaCepImpl.ERRO_FORMATO, cep);

			if (new String(data).equals(erroFormatado)) {
				erroFont = true;
				erro = erroFormatado;
				verificaErroFont = false;
			}
		}
	}

	public boolean isErro() {
		return erroTitle && erroFont;
	}

	public String getErro() {
		return erro;
	}

}
