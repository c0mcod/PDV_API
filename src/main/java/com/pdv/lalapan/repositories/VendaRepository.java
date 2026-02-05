package com.pdv.lalapan.repositories;

import com.pdv.lalapan.entities.Venda;
import com.pdv.lalapan.enums.StatusVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long>{
    Optional<Venda> findByStatus(StatusVenda status);

    // 1. Buscar vendas por período
    List<Venda> findByDataVendaBetween(LocalDate dataInicio, LocalDate dataFim);

    // 2. Calcular faturamento total por período
    @Query("SELECT SUM(v.valorTotal) FROM Venda v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularFaturamentoPorPeriodo(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    // 3. Contar número de vendas por período
    @Query("SELECT COUNT(v) FROM Venda v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim")
    Long contarVendasPorPeriodo(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    // 4. Calcular ticket médio (faturamento / número de vendas)
    @Query("SELECT AVG(v.valorTotal) FROM Venda v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularTicketMedio(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    // 5. Buscar vendas agrupadas por dia da semana
    @Query("SELECT FUNCTION('DAYOFWEEK', v.dataVenda) as dia, SUM(v.valorTotal) as total " +
            "FROM Venda v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY FUNCTION('DAYOFWEEK', v.dataVenda) " +
            "ORDER BY dia")
    List<Object[]> vendasPorDiaSemana(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    // 6. Contar total de produtos vendidos no período
    @Query("SELECT SUM(iv.quantidade) FROM ItemVenda iv " +
            "JOIN iv.venda v WHERE v.dataVenda BETWEEN :dataInicio AND :dataFim")
    Long contarProdutosVendidos(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}
