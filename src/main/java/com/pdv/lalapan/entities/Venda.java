package com.pdv.lalapan.entities;

import com.pdv.lalapan.enums.MetodoPagamento;
import com.pdv.lalapan.enums.StatusVenda;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHoraAbertura;
    private LocalDateTime dataHoraFechamento;

    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    private StatusVenda status;

    @OneToMany(
            mappedBy = "venda",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<VendaItens> itens = new ArrayList<>();

    // Construtor vazio
    public Venda() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHoraAbertura() {
        return dataHoraAbertura;
    }

    public void setDataHoraAbertura(LocalDateTime dataHoraAbertura) {
        this.dataHoraAbertura = dataHoraAbertura;
    }

    public LocalDateTime getDataHoraFechamento() {
        return dataHoraFechamento;
    }

    public void setDataHoraFechamento(LocalDateTime dataHoraFechamento) {
        this.dataHoraFechamento = dataHoraFechamento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public StatusVenda getStatus() {
        return status;
    }

    public void setStatus(StatusVenda status) {
        this.status = status;
    }

    public List<VendaItens> getItens() {
        return itens;
    }

    public void setItens(List<VendaItens> itens) {
        this.itens = itens;
    }

    public void adicionarItem(VendaItens item) {
        // Relacionamento bidirecional
        item.setVenda(this);

        // Adicionando a lista
        itens.add(item);

        // Recalcular o valorTotal
        RecalcularValorTotal();
    }

    private void RecalcularValorTotal() {
        /*
        *   Observação: "this" referencia ao atributo desta entidade
        *
        *   transcrição: valorTotal vai receber nossa lista de VendaItens passando por um fluxo de dados em pipelines(Stream)
        *                onde irá passar por uma operação intermédiaria(Map) que vai transformar cada elemento da stream em
        *                outro objeto, aplicando a função geSubtotal()(basicamente ele vai dizer: "pega cada item de VendaItens
        *                e pega só o subtotal de cada um.") e finalmente aplicando a operação terminal Reduce.
        *                Ela tem como função combinar todos os elementos encontrados pelo filtro anterior e somar.
        *
        *   Etapas: 1°: List<VendaItens> itens entra como Stream(um fluxo de dados/filtros/operações;
        *           2°: Para cada objeto VendaItens presente, extrair apenas o atributo subTotal;
        *           3°: Reduce irá acumular os resultados e somar, guardando cada resultado anterior para obter o valortotal;
        *           4°: tudo isso é armazenado em this.valorTotal que referencia ao atributo desta entidade.
        *
         */
        this.valorTotal = itens.stream()
                .map(VendaItens::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
