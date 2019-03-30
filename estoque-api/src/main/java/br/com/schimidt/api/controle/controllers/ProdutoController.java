package br.com.schimidt.api.controle.controllers;

import br.com.schimidt.api.controle.produto.Produto;
import br.com.schimidt.api.controle.produto.ProdutoDaoMock;

import java.util.List;
import java.util.stream.Collectors;

public class ProdutoController {

	private ProdutoDaoMock produtoDao;

	public ProdutoController() {
		produtoDao = new ProdutoDaoMock();
	}

	public List<Produto> lista() {
		return produtoDao.lista();
	}

	public List<Produto> filtra(String nome) {
		return produtoDao.lista().stream()
				.filter(produto -> produto.getNome().toLowerCase().startsWith(nome.toLowerCase()))
				.collect(Collectors.toList());
	}

	public List<Produto> filtra(String nome, String marca) {
		return produtoDao.lista().stream()
				.filter(produto ->
						produto.getNome().toLowerCase().startsWith(nome.toLowerCase())
								&& produto.getMarca().equalsIgnoreCase(marca)
				)
				.collect(Collectors.toList());
	}
}
