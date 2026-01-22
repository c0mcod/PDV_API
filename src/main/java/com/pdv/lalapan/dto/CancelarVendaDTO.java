package com.pdv.lalapan.dto;

import com.pdv.lalapan.enums.StatusVenda;

public record CancelarVendaDTO(Long vendaId, StatusVenda status) {
}
