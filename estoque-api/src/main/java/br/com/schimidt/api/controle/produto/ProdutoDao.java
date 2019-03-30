package br.com.schimidt.api.controle.produto;

import java.util.List;

interface ProdutoDao {

	List<Produto> lista();

	Produto getProduto(Integer id);
}
