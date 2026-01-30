package com.pdv.lalapan.dto;

import com.pdv.lalapan.enums.MetodoPagamento;

import java.math.BigDecimal;

public record VendaFinalizadaRequestDTO(MetodoPagamento metodo, BigDecimal valorRecebido) {
}
