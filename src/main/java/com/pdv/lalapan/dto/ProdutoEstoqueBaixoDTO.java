package com.pdv.lalapan.dto;

import com.pdv.lalapan.entities.Produto;

public record ProdutoEstoqueBaixoDTO(Long id, String nome, double quantidadeEstoque, double estoqueMinimo) {

    public static ProdutoEstoqueBaixoDTO fromEntity(Produto p) {
        return new ProdutoEstoqueBaixoDTO(
                p.getId(),
                p.getNome(),
                p.getQuantidadeEstoque(),
                p.getEstoqueMinimo()
        );
    }
}
