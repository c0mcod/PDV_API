package com.pdv.lalapan.dto;

import com.pdv.lalapan.entities.Venda;
import com.pdv.lalapan.entities.VendaItens;

import java.math.BigDecimal;

public record VendaAddItemResponseDTO(Long vendaId, BigDecimal valorTotal) {

}
