package com.pdv.lalapan.controllers;

import com.pdv.lalapan.dto.VendaAberturaDTO;
import com.pdv.lalapan.dto.VendaAddItemRequestDTO;
import com.pdv.lalapan.dto.VendaAddItemResponseDTO;
import com.pdv.lalapan.dto.VendaResponseDTO;
import com.pdv.lalapan.services.VendaService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venda")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    // Endpoint para iniciar venda
    @PostMapping("/abrir")
    public ResponseEntity<VendaAberturaDTO> abrirVenda() {
        VendaAberturaDTO venda = vendaService.iniciarVenda();
        return ResponseEntity.ok(venda);
    }

    @PostMapping("/{vendaId}/itens")
    public ResponseEntity<VendaAddItemResponseDTO> adicionarItens(@PathVariable Long vendaId, @RequestBody VendaAddItemRequestDTO requestDto) {
        VendaAddItemResponseDTO response = vendaService.adicionarItem(vendaId, requestDto);
        return ResponseEntity.ok(response);
    }
}
