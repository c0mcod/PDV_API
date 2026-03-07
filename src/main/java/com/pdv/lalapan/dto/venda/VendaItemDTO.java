package com.pdv.lalapan.dto.venda;

import com.pdv.lalapan.entities.VendaItens;

import java.math.BigDecimal;

public record VendaItemDTO(
        Long itemId,
        Long produtoId,
        String nomeProduto,
        BigDecimal precoUnitario,
        BigDecimal quantidade) {

    public VendaItemDTO(VendaItens item) {
        this(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getPrecoUnitario(),
                item.getQuantidade()
        );
    }
}
