/*
package com.pdv.lalapan.entities;

import com.pdv.lalapan.enums.StatusCaixa;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;

    private double saldoInicial;
    private double saldoFinal;

    private StatusCaixa status;

    @OneToMany(mappedBy = "caixa")
    private List<MovimentacaoCaixa> movimentacaoCaixa;

    @OneToMany(mappedBy = "caixa")
    private List<Venda> vendas = new ArrayList<>();

    // Construtor vazio
    public Caixa() {}

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public double getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public StatusCaixa getStatus() {
        return status;
    }

    public void setStatus(StatusCaixa status) {
        this.status = status;
    }

    public List<MovimentacaoCaixa> getMovimentacaoCaixa() {
        return movimentacaoCaixa;
    }

    public void setMovimentacaoCaixa(List<MovimentacaoCaixa> movimentacaoCaixa) {
        this.movimentacaoCaixa = movimentacaoCaixa;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }
}
*/