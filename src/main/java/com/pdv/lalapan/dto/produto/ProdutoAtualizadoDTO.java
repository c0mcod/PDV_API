package com.pdv.lalapan.dto.produto;

import com.pdv.lalapan.entities.Produto;
import com.pdv.lalapan.enums.Categoria;
import com.pdv.lalapan.enums.Unidade;

import java.math.BigDecimal;

public record ProdutoAtualizadoDTO(
        String nome,
        BigDecimal preco,
        String codigo,
        BigDecimal precoCusto,
        BigDecimal quantidadeEstoque,
        Unidade unidade,
        Categoria categoria
) {

    public static ProdutoAtualizadoDTO fromEntity(Produto produto) {
        return new ProdutoAtualizadoDTO(
                produto.getNome(),
                produto.getPreco(),
                produto.getCodigo(),
                produto.getPrecoCusto(),
                produto.getQuantidadeEstoque(),
                produto.getUnidade(),
                produto.getCategoria()
        );
    }
}
