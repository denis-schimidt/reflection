package br.com.schimidt.alurator.playground.controle;

import java.util.ArrayList;
import java.util.List;

public class Controle {
	private List<String> lista = new ArrayList<String>() {{
		add("item 1");
		add("item 2");
		add("item 3");
	}};

	public Controle() {
	}

	public Controle(String param1) {
	}

	public Controle(String param1, String param2) {
	}

	public List<String> getLista() {
		return lista;
	}
}
