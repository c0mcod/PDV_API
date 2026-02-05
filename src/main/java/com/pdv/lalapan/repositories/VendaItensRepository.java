package com.pdv.lalapan.repositories;

import com.pdv.lalapan.entities.VendaItens;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VendaItensRepository extends JpaRepository<VendaItens, Long> {

    // 7. Top 5 produtos mais vendidos
    @Query("SELECT p.nome, SUM(iv.quantidade) as qtd, SUM(iv.quantidade * iv.precoUnitario) as total " +
            "FROM ItemVenda iv " +
            "JOIN iv.produto p " +
            "JOIN iv.venda v " +
            "WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY p.id, p.nome " +
            "ORDER BY qtd DESC")
    List<Object[]> topProdutosMaisVendidos(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable
    );

    // 8. Vendas agrupadas por categoria
    @Query("SELECT c.nome, SUM(iv.quantidade * iv.precoUnitario) as total " +
            "FROM ItemVenda iv " +
            "JOIN iv.produto p " +
            "JOIN p.categoria c " +
            "JOIN iv.venda v " +
            "WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY c.id, c.nome " +
            "ORDER BY total DESC")
    List<Object[]> vendasPorCategoria(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}
