package br.com.schimidt.alurator;

import br.com.schimidt.alurator.protocolo.Request;
import br.com.schimidt.alurator.reflexao.Reflexao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Alurator {
	private String nomePacote;

	public Alurator(String nomePacote) {
		this.nomePacote = nomePacote;
	}

	public Object executa(String url) {
		Request request = new Request(url);

		return new Reflexao()
				.refletirClasse(nomePacote, request.getNomeController())
				.criarInstancia()
				.getMetodo(request.getNomeMetodoAlvo(), request.getParametros())
				.comTratamentoEspecialExcecao((Method metodo, InvocationTargetException excecao) -> {
					System.out.println("Erro no método " + metodo.getName() + " da classe " + metodo.getDeclaringClass().getName() + ".\n\n");
					throw new RuntimeException(excecao.getTargetException());
				})
				.invocar();
	}
}
