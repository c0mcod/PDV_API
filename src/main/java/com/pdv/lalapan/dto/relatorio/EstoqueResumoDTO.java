package com.pdv.lalapan.dto.relatorio;

import java.math.BigDecimal;

public record EstoqueResumoDTO(
        BigDecimal valorTotalEmEstoque,
        Integer produtosCriticos,
        Integer produtosBaixos,
        Integer totalProdutosAtivos,
        Integer ok
) {
}
