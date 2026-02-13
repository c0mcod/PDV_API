package com.pdv.lalapan.dto.produto;

import com.pdv.lalapan.enums.Categoria;
import com.pdv.lalapan.enums.Unidade;

import java.math.BigDecimal;

public record ProdutoCreatedDTO(
        String nome,
        String codigo,
        BigDecimal estoqueMinimo,
        BigDecimal preco,
        BigDecimal precoCusto,
        Unidade unidade,
        Categoria categoria,
        BigDecimal quantidadeEstoque
) {

}
