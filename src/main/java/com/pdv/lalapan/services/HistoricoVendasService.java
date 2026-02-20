package com.pdv.lalapan.services;

import com.pdv.lalapan.dto.historicoVendas.HistoricoVendasResponseDTO;
import com.pdv.lalapan.entities.Venda;
import com.pdv.lalapan.repositories.VendaRepository;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistoricoVendasService {

    private final VendaRepository vendaRepo;

    public HistoricoVendasService(VendaRepository vendaRepo) {
        this.vendaRepo = vendaRepo;
    }

    public Page<HistoricoVendasResponseDTO> buscarHistorico(LocalDateTime dataInicio, LocalDateTime dataFim, Long operadorId, Pageable pageable) {
        return vendaRepo.buscarHistorico(dataInicio, dataFim, operadorId, pageable)
        .map(venda -> new HistoricoVendasResponseDTO(
                venda.getId(),
                venda.getDataHoraAbertura(),
                venda.getDataHoraFechamento(),
                venda.getOperador().getNome(),
                venda.getValorTotal(),
                venda.getItens().size()
        ));
    }
}
