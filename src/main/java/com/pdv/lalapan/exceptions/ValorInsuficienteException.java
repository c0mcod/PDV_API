package com.pdv.lalapan.exceptions;

import java.math.BigDecimal;

public class ValorInsuficienteException extends RuntimeException {
    private BigDecimal valorRecebido;
    private BigDecimal totalVenda;

    public ValorInsuficienteException(BigDecimal valorRecebido, BigDecimal totalVenda) {
        super(String.format("Valor recebido (R$%d) é menor que o total da venda (R$%d)", valorRecebido, totalVenda));
        this.totalVenda = totalVenda;
        this.valorRecebido = valorRecebido;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public BigDecimal getTotalVenda() {
        return totalVenda;
    }
}
