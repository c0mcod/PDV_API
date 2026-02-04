package com.pdv.lalapan.dto;

import com.pdv.lalapan.entities.Produto;

import java.math.BigDecimal;

public record ProdutoEstoqueBaixoDTO(Long id, String nome, BigDecimal quantidadeEstoque, BigDecimal estoqueMinimo) {

    public static ProdutoEstoqueBaixoDTO fromEntity(Produto p) {
        return new ProdutoEstoqueBaixoDTO(
                p.getId(),
                p.getNome(),
                p.getQuantidadeEstoque(),
                p.getEstoqueMinimo()
        );
    }
}
