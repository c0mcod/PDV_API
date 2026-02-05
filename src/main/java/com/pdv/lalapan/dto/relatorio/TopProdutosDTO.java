package com.pdv.lalapan.dto.relatorio;

import java.math.BigDecimal;

public record TopProdutosDTO(int posicao, String nome, BigDecimal quantidadeVendas, BigDecimal valorTotal) {
}
