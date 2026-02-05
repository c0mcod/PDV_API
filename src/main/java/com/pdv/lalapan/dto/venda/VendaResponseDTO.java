package com.pdv.lalapan.dto.venda;

import com.pdv.lalapan.entities.Venda;
import com.pdv.lalapan.enums.StatusVenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VendaResponseDTO(
        Long id,
        LocalDateTime dataHoraAbertura,
        LocalDateTime dataHoraFechamento,
        StatusVenda status,
        BigDecimal valorTotal
) {
    public VendaResponseDTO (Venda venda) {
        this(
                venda.getId(),
                venda.getDataHoraAbertura(),
                venda.getDataHoraFechamento(),
                venda.getStatus(),
                venda.getValorTotal()
        );
    }
}
