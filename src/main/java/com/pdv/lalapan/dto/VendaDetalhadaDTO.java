package com.pdv.lalapan.dto;

import com.pdv.lalapan.entities.VendaItens;

import java.math.BigDecimal;
import java.util.List;

public record VendaDetalhadaDTO(Long id, BigDecimal valorTotal, List<VendaItemDTO> itens) {
}
