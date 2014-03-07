package br.com.objectos;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

public class DetalheParser extends ParserCallback {

	private final Map<String, String> enderecoMap = new HashMap<String, String>();

	private boolean copiaKey;
	private boolean insereValue;
	private String keyAtual;

	@Override
	public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
		if (t == HTML.Tag.TD) {
			if (a.containsAttribute(HTML.Attribute.CLASS, "label")) {
				copiaKey = true;
			}

			if (a.containsAttribute(HTML.Attribute.CLASS, "value")) {
				insereValue = true;
			}
		}
	}

	@Override
	public void handleText(char[] data, int pos) {
		if (copiaKey) {
			keyAtual = new String(data);
			copiaKey = false;
		}

		if (insereValue) {
			enderecoMap.put(keyAtual, new String(data));
			insereValue = false;
		}
	}

	public Map<String, String> getEnderecoMap() {
		return enderecoMap;
	}

}
