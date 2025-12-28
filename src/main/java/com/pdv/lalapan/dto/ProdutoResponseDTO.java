package com.pdv.lalapan.dto;

import com.pdv.lalapan.entities.Produto;
import com.pdv.lalapan.enums.Categoria;
import com.pdv.lalapan.enums.Unidade;

import java.math.BigDecimal;

public record ProdutoResponseDTO(Long id, String nome, BigDecimal Preco, Unidade unidade, Categoria categoria) {

    public ProdutoResponseDTO(Produto entity) {
        this(entity.getId(), entity.getNome(), entity.getPreco(), entity.getUnidade(), entity.getCategoria());
    }
}
