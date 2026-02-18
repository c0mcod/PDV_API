package com.pdv.lalapan.dto.venda;

import com.pdv.lalapan.enums.MetodoPagamento;

import java.math.BigDecimal;

public record PagamentoRequestDTO(MetodoPagamento metodo, BigDecimal valor) {
}
