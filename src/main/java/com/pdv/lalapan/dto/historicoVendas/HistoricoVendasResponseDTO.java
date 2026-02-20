package com.pdv.lalapan.dto.historicoVendas;

import com.pdv.lalapan.entities.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoricoVendasResponseDTO(
    Long vendaId,
    LocalDateTime dataHoraAbertura,
    LocalDateTime dataHoraFechamento,
    String operadorNome,
    BigDecimal valorTotal,
    int totalItens
) {
}
