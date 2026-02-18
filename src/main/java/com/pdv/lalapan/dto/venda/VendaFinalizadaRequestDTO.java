package com.pdv.lalapan.dto.venda;

import com.pdv.lalapan.entities.Pagamento;

import java.util.List;

public record VendaFinalizadaRequestDTO(List<PagamentoRequestDTO> pagamentos) {
}
