package com.pdv.lalapan.controllers;

import com.pdv.lalapan.dto.produto.*;
import com.pdv.lalapan.services.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> create(@RequestBody ProdutoCreatedDTO dto) {
        ProdutoResponseDTO response = produtoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/atualiza/{produtoId}")
    public ResponseEntity<ProdutoAtualizadoDTO> updateProduct(@RequestBody ProdutoAtualizadoDTO dto, @PathVariable Long produtoId) {
        ProdutoAtualizadoDTO response = produtoService.atualizarProduto(produtoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarProdutoPorId(@PathVariable Long id) {
        ProdutoResponseDTO produtoEncontrado = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produtoEncontrado);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<ProdutoResponseDTO>> procurarTodosProdutos() {
        return ResponseEntity.ok(produtoService.buscarTodosProdutos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ProdutoEstoqueBaixoDTO>> listarEstoqueBaixo() {
        List<ProdutoEstoqueBaixoDTO> produtos = produtoService.listarEstoqueBaixo();
        return ResponseEntity.ok(produtos);
    }

    @PostMapping("/{id}/adicionar-estoque")
    public ResponseEntity<ProdutoResponseDTO> adicionar(@PathVariable Long id, @RequestBody EntradaProdutoRequestDTO request) {
        ProdutoResponseDTO produtoAtualizado = produtoService.registrarEntrada(id, request);
        return ResponseEntity.ok(produtoAtualizado);
    }
}
