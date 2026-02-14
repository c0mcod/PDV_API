package com.pdv.lalapan.services;

import com.pdv.lalapan.dto.cancelamento.CancelarItemDTO;
import com.pdv.lalapan.dto.cancelamento.CancelarVendaDTO;
import com.pdv.lalapan.dto.venda.*;
import com.pdv.lalapan.entities.Produto;
import com.pdv.lalapan.entities.Venda;
import com.pdv.lalapan.enums.Categoria;
import com.pdv.lalapan.enums.MetodoPagamento;
import com.pdv.lalapan.enums.StatusVenda;
import com.pdv.lalapan.enums.Unidade;
import com.pdv.lalapan.exceptions.EstoqueInsuficienteException;
import com.pdv.lalapan.exceptions.VendaNaoAbertaException;
import com.pdv.lalapan.exceptions.VendaNaoEncontradaException;
import com.pdv.lalapan.repositories.ProdutoRepository;
import com.pdv.lalapan.repositories.VendaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VendaServiceTest {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private ProdutoRepository prodRepo;

    @Autowired
    private VendaRepository vendaRepo;

    private Produto criarProduto(String nome, BigDecimal estoque) {
        Produto p = new Produto();
        p.setNome(nome);
        p.setPreco(new BigDecimal("10.00"));
        p.setCodigo("1200050123");
        p.setEstoqueMinimo(new BigDecimal("5.00"));
        p.setQuantidadeEstoque(estoque);
        p.setUnidade(Unidade.UN);
        p.setCategoria(Categoria.BEBIDAS);

        return prodRepo.save(p);
    }

    // ========== TESTES DE MÉTODOS =========
    @Test
    void deveCriarVendaComSucesso() {
        VendaAberturaDTO venda = vendaService.iniciarVenda();

        assertNotNull(venda);
        assertNotNull(venda.vendaId());
    }

    @Test
    void deveAdicionarItemNaVenda() {
        // ====== ARRANGE ======
        Produto produto = criarProduto("LEITE", new BigDecimal("50.00"));
        VendaAberturaDTO novaVenda = vendaService.iniciarVenda();

        // ====== ACT ======
        VendaAddItemResponseDTO response = vendaService.adicionarItem(
                novaVenda.vendaId(),
                new VendaAddItemRequestDTO(produto.getId(), new BigDecimal("5.00"))
        );

        // ====== ASSERT ======
        assertNotNull(response);
        assertEquals(new BigDecimal("100.00"), response.valorTotal());

    }

    @Test
    void deveFinalizarVenda() {
        // ====== ARRANGE ======
        Produto produto = criarProduto("SUCO DE MARACUJÁ", new BigDecimal("100.00"));
        VendaAberturaDTO novaVenda = vendaService.iniciarVenda();

        VendaAddItemResponseDTO response = vendaService.adicionarItem(
                novaVenda.vendaId(),
                new VendaAddItemRequestDTO(produto.getId(), new BigDecimal("5.00"))
        );

        BigDecimal valorRecebido = new BigDecimal("50.00");

        // ====== ACT ======
        vendaService.fecharVenda(
                novaVenda.vendaId(),
                new VendaFinalizadaRequestDTO(MetodoPagamento.PIX, valorRecebido)
        );

        // ====== ASSERT ======
        Produto atualizado = prodRepo.findById(produto.getId()).get();
        assertEquals(95.0, atualizado.getQuantidadeEstoque());
    }

    @Test
    void deveForcarFechamentoComEstoqueInsuficiente() {
        // ====== ARRANGE ======
        // Definido que estoque para sabão é 5.
        Produto produto = criarProduto("SABÃO", new BigDecimal("5.00"));
        VendaAberturaDTO venda = vendaService.iniciarVenda();
        vendaService.adicionarItem(venda.vendaId(), new VendaAddItemRequestDTO(produto.getId(), new BigDecimal("6")));

        BigDecimal valorRecebido = new BigDecimal("60.00");
        // ====== ACT ======
        VendaFinalizadaResponseDTO response = vendaService.forcarVendaFechada(
                venda.vendaId(),
                new VendaFinalizadaRequestDTO(MetodoPagamento.DEBITO, valorRecebido)
        );

        // ====== ASSERT ======
        assertNotNull(response);

        Produto atualizado = prodRepo.findById(produto.getId()).get();
        assertEquals(-1.0, atualizado.getQuantidadeEstoque());
        assertTrue(atualizado.getQuantidadeEstoque().equals(BigDecimal.ZERO));
    }

    @Test
    void deveCancelarVenda() {
        // ====== ARRANGE ======
        Produto produto = criarProduto("FEIJÃO", new BigDecimal("100.00"));
        VendaAberturaDTO novaVenda = vendaService.iniciarVenda();
        vendaService.adicionarItem(
                novaVenda.vendaId(),
                new VendaAddItemRequestDTO(produto.getId(), new BigDecimal("10.00"))
        );

        // ====== ACT ======
        CancelarVendaDTO response = vendaService.cancelarVenda(novaVenda.vendaId());

        // ====== ASSERT ======
        assertNotNull(response);
        assertEquals(StatusVenda.CANCELADA, response.status());
    }

    @Test
    void deveCancelarItemVenda() {
        // ====== ARRANGE ======
        Produto produto = criarProduto("ARROZ", new BigDecimal("100.00"));
        Produto produto2 = criarProduto("CARNE", new BigDecimal("100.00"));

        VendaAberturaDTO novaVenda = vendaService.iniciarVenda();

        // Produto 1
        vendaService.adicionarItem(
                novaVenda.vendaId(),
                new VendaAddItemRequestDTO(produto.getId(), new BigDecimal("2.00")) // R$20,00
        );

        // Produto 2
        vendaService.adicionarItem(
                novaVenda.vendaId(),
                new VendaAddItemRequestDTO(produto2.getId(), new BigDecimal("5.00")) // R$50,00
        );
        // com os dois deve ter R$70,00 de valor total, mas como foi removido, só há R$20,00.

        // ====== ACT ======
        CancelarItemDTO response = vendaService.cancelarItem(
                novaVenda.vendaId(),
                produto2.getId()
        );

        // ====== ASSERT ======
        assertNotNull(response);

        Venda vendaAtualizada = vendaRepo.findById(novaVenda.vendaId()).get();

        assertEquals(1, vendaAtualizada.getItens().size());
        assertEquals(produto.getId(), vendaAtualizada.getItens().get(0).getProduto().getId());
        assertEquals(0, vendaAtualizada.getValorTotal().compareTo(new BigDecimal("20.00")));
    }

    // ========== TESTES DE EXCEPTIONS ==========
    @Test
    void naoDeveFinalizarVendaEstoqueInsuficiente() {
        VendaAberturaDTO venda = vendaService.iniciarVenda();
        Produto produto = criarProduto("feijão", new BigDecimal(0.00));

        vendaService.adicionarItem(
                venda.vendaId(),
                new VendaAddItemRequestDTO(produto.getId(), new BigDecimal("1"))
        );

        BigDecimal valorRecebido = new BigDecimal("10.00");

        assertThrows(
                EstoqueInsuficienteException.class,
                () -> vendaService.fecharVenda(venda.vendaId(), new VendaFinalizadaRequestDTO(MetodoPagamento.PIX, valorRecebido))
        );
    }

    @Test
    void naoDeveAdicionarItemComVendaNaoAberta() {
        VendaAberturaDTO venda = vendaService.iniciarVenda();
        Produto produto = criarProduto("arroz", new BigDecimal("5"));

        Venda vendaAtualizada = vendaRepo.findById(venda.vendaId()).get();

        vendaService.adicionarItem(
                venda.vendaId(),
                new VendaAddItemRequestDTO(produto.getId(), new BigDecimal("1"))
        );

        BigDecimal valorRecebido = new BigDecimal("10.00");
        vendaService.fecharVenda(venda.vendaId(), new VendaFinalizadaRequestDTO(MetodoPagamento.PIX, valorRecebido));

        assertThrows(
                VendaNaoAbertaException.class,
                () -> vendaService.adicionarItem(venda.vendaId(), new VendaAddItemRequestDTO(produto.getId(), new BigDecimal("1")))
                );
    }

    @Test
    void naoDeveAdicionarProdutoEmVendaNaoEncontrada() {
        Produto produto = criarProduto("arroz", new BigDecimal("5.00"));

        assertThrows(VendaNaoEncontradaException.class,
                () -> vendaService.adicionarItem(999L, new VendaAddItemRequestDTO(produto.getId(), new BigDecimal("1.00")))
                );
    }

}
