package br.com.schimidt.alurator.protocolo;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class QueryParamsBuilder {
	private Map<String, Object> queryParams;

	public QueryParamsBuilder withParams(String stringQueryParams) {
		Map<String, Object> chaveEValorModificavel = new LinkedHashMap<>();
		String[] stringParams = stringQueryParams.split("&");
		
		for (String stringParam : stringParams) {
			String[] chaveEValor = stringParam.split("=");
			
			String chave = chaveEValor[0];
			Object valor = chaveEValor[1];

			chaveEValorModificavel.put(chave, valor);
		}

		queryParams = unmodifiableMap(chaveEValorModificavel);

		return this;
	}
	
	public Map<String, Object> getMapeamentoChaveValor() {
		return this.queryParams;
	}
}
