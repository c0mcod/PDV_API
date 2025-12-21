/*
package com.pdv.lalapan.repositories;

import com.pdv.lalapan.entities.MovimentacaoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovimentacaoCaixaRepository extends JpaRepository<MovimentacaoCaixa, Long> {

    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM MovimentacaoCaixa m WHERE m.caixa.id = :caixaId")
    double somarMovimentacoes(@Param("CaixaId") Long caixaId);
}
*/
