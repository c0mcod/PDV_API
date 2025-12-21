package com.pdv.lalapan.dto;

import com.pdv.lalapan.enums.MetodoPagamento;

import java.time.LocalDateTime;

public class VendaDTO {
    private LocalDateTime dataHora;
    private double valorTotal;
    private MetodoPagamento metodoPagamento;

    // Construtor vazio
    public VendaDTO() {}

    // Getters e Setters
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}
