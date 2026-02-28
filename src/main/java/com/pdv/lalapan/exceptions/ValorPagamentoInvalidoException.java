package com.pdv.lalapan.exceptions;

import java.math.BigDecimal;

public class ValorPagamentoInvalidoException extends RuntimeException {
    BigDecimal valor;

    public ValorPagamentoInvalidoException(BigDecimal valor) {
        super(String.format("%.2f é invalido.", valor));
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
