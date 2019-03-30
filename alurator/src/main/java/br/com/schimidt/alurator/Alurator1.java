package br.com.schimidt.alurator;

public class Alurator1 {

	private String nomePacote;

	public Alurator1(String nomePacote) {
		this.nomePacote = nomePacote;
	}

	public Object executa(String url) {
		ExtratoraClasseComMetodoAlvo extratoraClasseComMetodoAlvo = new ExtratoraClasseComMetodoAlvo(nomePacote);

		return extratoraClasseComMetodoAlvo.obterDe(url)
				.map(classeAlvo-> classeAlvo.newInstance())
				.orElse(null);
	}
}
