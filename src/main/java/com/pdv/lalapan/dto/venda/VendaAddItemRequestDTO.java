package com.pdv.lalapan.dto.venda;

import java.math.BigDecimal;

public record VendaAddItemRequestDTO(Long idProduto, BigDecimal quantidade) {

}
