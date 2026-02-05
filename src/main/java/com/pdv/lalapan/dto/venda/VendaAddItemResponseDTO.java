package com.pdv.lalapan.dto.venda;

import java.math.BigDecimal;

public record VendaAddItemResponseDTO(Long vendaId, BigDecimal valorTotal, Long itemId) {

}
