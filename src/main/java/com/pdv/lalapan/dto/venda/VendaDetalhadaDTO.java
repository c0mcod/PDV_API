package com.pdv.lalapan.dto.venda;

import java.math.BigDecimal;
import java.util.List;

public record VendaDetalhadaDTO(Long id, BigDecimal valorTotal, List<VendaItemDTO> itens) {
}
