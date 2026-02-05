package com.pdv.lalapan.dto.relatorio;

import com.pdv.lalapan.enums.Categoria;

import java.math.BigDecimal;

public record CategoriaSalesDTO(Categoria categoria, BigDecimal valorVendas, BigDecimal percentualParticipacao) {
}
