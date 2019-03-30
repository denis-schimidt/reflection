package br.com.schimidt.alurator.protocolo;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class Request {
	private static final String SUFIXO_CLASSE_CONTROLLER = "Controller";
	private static final String DELIMITADOR = "/";
	private static final int INDICE_NOME_CONTROLLER = 0;
	private static final int INDICE_METODO_CONTROLLER = 1;
	private static final int INDICE_PARAMETROS_URI = 1;
	private static final int INDICE_URL = 0;
	private final String nomeController;
	private final String nomeMetodoAlvo;
	private Map<String, Object> parametros;

	public Request(String uri) {
		String[] partesUri = uri.split("[?]");
		String urlComPath = uri;

		if (partesUri.length > 1) {
			parametros = new QueryParamsBuilder().withParams(partesUri[INDICE_PARAMETROS_URI]).getMapeamentoChaveValor();
			urlComPath = partesUri[INDICE_URL];
		}

		String[] parteNomeClasseComMetodo = urlComPath.replaceFirst("^/", "")
				.split(DELIMITADOR);

		if (parteNomeClasseComMetodo.length == 0) {
			throw new IllegalArgumentException("Argumentos inválidos!");
		}

		nomeController = parteNomeClasseComMetodo[INDICE_NOME_CONTROLLER].concat(SUFIXO_CLASSE_CONTROLLER);
		nomeMetodoAlvo = parteNomeClasseComMetodo[INDICE_METODO_CONTROLLER];
	}

	public String getNomeController() {
		return nomeController;
	}

	public String getNomeMetodoAlvo() {
		return nomeMetodoAlvo;
	}

	public Map<String, Object> getParametros() {
		return ofNullable(parametros).orElseGet(()-> new HashMap<>());
	}
}
