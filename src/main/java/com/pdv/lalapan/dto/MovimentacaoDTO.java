package com.pdv.lalapan.dto;

import com.pdv.lalapan.enums.MetodoPagamento;

public class MovimentacaoDTO {
    private MetodoPagamento tipo;
    private double valor;

    // Construtor vazio
    public MovimentacaoDTO() {}

    // Getters e Setters

    public MetodoPagamento getTipo() {
        return tipo;
    }

    public void setTipo(MetodoPagamento tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
