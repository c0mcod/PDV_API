package com.pdv.lalapan.controllers;

import com.pdv.lalapan.dto.cancelamento.CancelarItemDTO;
import com.pdv.lalapan.dto.cancelamento.CancelarVendaDTO;
import com.pdv.lalapan.dto.cancelamento.RemoverItemRequest;
import com.pdv.lalapan.dto.venda.*;
import com.pdv.lalapan.services.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venda")
@Tag(
        name = "Vendas",
        description = "Operações relacionadas ao ciclo de venda no PDV (abrir venda, adicionar itens, finalizar ou cancelar)"
)
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    // ========== CICLO PRINCIPAL ==========

    @Operation(
            summary = "Abrir nova venda",
            description = "Inicia uma nova venda vinculada a um usuário operador do PDV."
    )
    @PostMapping("/abrir")
    public ResponseEntity<VendaAberturaDTO> abrirVenda(@RequestParam Long usuarioId) {
        VendaAberturaDTO venda = vendaService.iniciarVenda(usuarioId);
        return ResponseEntity.ok(venda);
    }

    @Operation(
            summary = "Adicionar item à venda",
            description = "Adiciona um produto à venda aberta. A venda deve estar em status ABERTA."
    )
    @PostMapping("/{vendaId}/itens")
    public ResponseEntity<VendaAddItemResponseDTO> adicionarItens(@PathVariable Long vendaId, @RequestBody VendaAddItemRequestDTO requestDto) {
        VendaAddItemResponseDTO response = vendaService.adicionarItem(vendaId, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Finalizar venda",
            description = "Fecha a venda aplicando forma de pagamento e calculando o valor final."
    )
    @PostMapping("/{vendaId}/finalizar")
    public ResponseEntity<VendaFinalizadaResponseDTO> finalizarVenda(@PathVariable Long vendaId, @RequestBody VendaFinalizadaRequestDTO requestDTO) {
        VendaFinalizadaResponseDTO response = vendaService.fecharVenda(vendaId, requestDTO);
        return ResponseEntity.ok(response);
    }

    // ========== FUNÇÕES AUXILIARES ==========

    @Operation(
            summary = "Remover item da venda",
            description = "Remove ou cancela um item específico da venda antes da finalização."
    )
    @PostMapping("/{vendaId}/remover-item")
    public ResponseEntity<CancelarItemDTO> cancelarItem(@PathVariable Long vendaId, @RequestBody RemoverItemRequest request) {
        CancelarItemDTO response = vendaService.cancelarItem(vendaId, request.itemId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cancelar venda",
            description = "Cancela completamente uma venda aberta. Todos os itens são descartados."
    )
    @PostMapping("/{vendaId}/cancelar-venda")
    public ResponseEntity<CancelarVendaDTO> cancelarVenda(@PathVariable Long vendaId) {
        CancelarVendaDTO response = vendaService.cancelarVenda(vendaId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar detalhes da venda",
            description = "Retorna todas as informações da venda incluindo itens, valores e status."
    )
    @GetMapping("/{vendaId}")
    public ResponseEntity<VendaDetalhadaDTO> buscarVenda(@PathVariable Long vendaId) {
        return ResponseEntity.ok(vendaService.buscarVendaDetalhada(vendaId));
    }

}
