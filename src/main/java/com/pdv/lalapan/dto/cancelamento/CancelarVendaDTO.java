package com.pdv.lalapan.dto.cancelamento;

import com.pdv.lalapan.enums.StatusVenda;

public record CancelarVendaDTO(Long vendaId, StatusVenda status) {
}
