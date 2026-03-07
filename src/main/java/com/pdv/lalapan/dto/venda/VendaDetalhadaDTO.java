package com.pdv.lalapan.dto.venda;

import com.pdv.lalapan.entities.Venda;

import java.math.BigDecimal;
import java.util.List;

public record VendaDetalhadaDTO(
        Long id,
        BigDecimal valorTotal,
        List<VendaItemDTO> itens) {

    public VendaDetalhadaDTO(Venda venda) {
        this(
                venda.getId(),
                venda.getValorTotal(),
                venda.getItens()
                        .stream()
                        .map(VendaItemDTO::new)
                        .toList()
        );
    }

}
