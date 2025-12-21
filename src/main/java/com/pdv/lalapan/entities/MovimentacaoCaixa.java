/*
package com.pdv.lalapan.entities;

import com.pdv.lalapan.enums.MetodoPagamento;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class MovimentacaoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private MetodoPagamento tipo;
    private double valor;
    private String descricao;
    private LocalDateTime dataHora;

    */

    /*
    @ManyToOne
    @JoinColumn(name="caixa_id", nullable = false)
    private Caixa caixa;
    */

    /*
    @OneToOne
    @JoinColumn(name = "venda_id", nullable = true)
    private Venda venda;

    // Construtor vazio
    public MovimentacaoCaixa() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }
}
*/