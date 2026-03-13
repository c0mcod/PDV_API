package com.pdv.lalapan.dto.impressao;

import com.pdv.lalapan.dto.venda.PagamentoRequestDTO;
import com.pdv.lalapan.dto.venda.VendaItemDTO;
import com.pdv.lalapan.entities.Pagamento;
import com.pdv.lalapan.entities.VendaItens;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ImpressaoDTO(
        Long vendaId,
        LocalDateTime horaAbertura,
        LocalDateTime horaFechamento,
        List<VendaItemDTO> itens,
        BigDecimal valorTotal,
        BigDecimal troco,
        List<PagamentoRequestDTO> pagamentos
) {


}
