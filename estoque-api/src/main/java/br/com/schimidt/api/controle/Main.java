package br.com.schimidt.api.controle;

import br.com.schimidt.alurator.Alurator;
import br.com.schimidt.api.controle.controllers.ProdutoController;
import br.com.schimidt.api.controle.produto.Produto;

import java.util.Scanner;

public class Main {

	/**
	 * Simula o navegador.
	 * 
	 */
	public static void main(String[] args) throws Exception {
		
		try (Scanner s = new Scanner(System.in)) {
			String url = s.nextLine();
			
			Alurator alurator = new Alurator(ProdutoController.class.getPackage().getName());
			while (!url.equals("exit")) {
				Object response = alurator.executa(url);

				System.out.println("Response: " + response);
				
				url = s.nextLine();
			}
		}
	}
}
