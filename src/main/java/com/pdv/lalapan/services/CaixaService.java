/*
package com.pdv.lalapan.services;

import com.pdv.lalapan.dto.MovimentacaoDTO;
import com.pdv.lalapan.entities.Caixa;
import com.pdv.lalapan.entities.MovimentacaoCaixa;
import com.pdv.lalapan.enums.StatusCaixa;
import com.pdv.lalapan.repositories.CaixaRepository;
import com.pdv.lalapan.repositories.MovimentacaoCaixaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CaixaService {
    private final CaixaRepository caRepo;
    private final MovimentacaoCaixaRepository movRepo;

    public CaixaService(CaixaRepository caRepo, MovimentacaoCaixaRepository movRepo) {
        this.caRepo = caRepo; this.movRepo = movRepo;
    }

    public Caixa abrirCaixa(float saldoInicial) {

        //Verifica se não tem mais de um caixa aberto
        if(caRepo.existsByStatus(StatusCaixa.ABERTO)) {
            throw new IllegalStateException("Já existe caixa aberto");
        }

        // Settar valor iniciais do caixa
        Caixa caixa = new Caixa();
        caixa.setDataAbertura(LocalDateTime.now());
        caixa.setSaldoInicial(saldoInicial);
        caixa.setStatus(StatusCaixa.ABERTO);

        return caRepo.save(caixa);
    }

    public Caixa registrarMovimentacao(Long caixaId, MovimentacaoDTO dto) {

        Caixa caixa = caRepo.findById(caixaId)
                .orElseThrow(() -> new IllegalArgumentException("Caixa inexistente."));

        if (caixa.getStatus() != StatusCaixa.ABERTO) {
            throw new IllegalStateException("Não é possivel registrar movimentação em caixa fechado.");
        }

        MovimentacaoCaixa mov = new MovimentacaoCaixa();
        mov.setCaixa(caixa);
        mov.setTipo(dto.getTipo());
        mov.setValor(dto.getValor());
        mov.setDataHora(LocalDateTime.now());

        movRepo.save(mov);

        return caixa;
    }

    public Caixa fecharCaixa(Long caixaId) {
        Caixa caixa = caRepo.findById(caixaId)
                .orElseThrow(() -> new IllegalArgumentException("Caixa inexistente."));

        if (caixa.getStatus() != StatusCaixa.ABERTO) {
            throw new IllegalStateException("O caixa já está fechado.");
        }

        double totalMov = movRepo.somarMovimentacoes(caixaId);

        caixa.setDataFechamento(LocalDateTime.now());
        caixa.setSaldoFinal(caixa.getSaldoInicial() + totalMov);
        caixa.setStatus(StatusCaixa.FECHADO);

        return caRepo.save(caixa);
    }
}


 */