package com.pdv.lalapan.services;

import com.pdv.lalapan.dto.produto.EntradaProdutoRequestDTO;
import com.pdv.lalapan.dto.produto.ProdutoEstoqueBaixoDTO;
import com.pdv.lalapan.entities.Produto;
import com.pdv.lalapan.enums.Categoria;
import com.pdv.lalapan.enums.Unidade;
import com.pdv.lalapan.exceptions.ProdutoInexistenteException;
import com.pdv.lalapan.exceptions.QuantidadeInvalidaException;
import com.pdv.lalapan.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ProdutoServiceTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    private Produto criarProduto(String nome, double estoque) {
        Produto p = new Produto();
        p.setNome(nome);
        p.setPreco(new BigDecimal("10.00"));
        p.setCodigo("1200050123");
        p.setEstoqueMinimo(10.00);
        p.setQuantidadeEstoque(estoque);
        p.setUnidade(Unidade.L);
        p.setCategoria(Categoria.BEBIDAS);

        return produtoRepository.save(p);
    }

    // ========== TESTES DE MÉTODOS ==========
    @Test
    void deveRegistrarEntradaEstoque() {
        Produto p = criarProduto("BATATA", 10.0);

        produtoService.registrarEntrada(
                p.getId(),
                new EntradaProdutoRequestDTO("RECEBIMENTO DE BATATAS", 50.0));

        Produto produtoAtualizado = produtoRepository.findById(p.getId()).get();

        assertEquals(60.0, produtoAtualizado.getQuantidadeEstoque());
    }

    @Test
    void deveListarProdutosEstoqueBaixo() {
        Produto baixo1 = criarProduto("feijão", 40.0);
        baixo1.setEstoqueMinimo(50);
        produtoRepository.save(baixo1);

        Produto baixo2 = criarProduto("arroz", 9);
        baixo2.setEstoqueMinimo(10);
        produtoRepository.save(baixo2);

        Produto ok = criarProduto("batata", 10);
        ok.setEstoqueMinimo(5);
        produtoRepository.save(ok);

        List<ProdutoEstoqueBaixoDTO> produtoEstoqueBaixo = produtoService.listarEstoqueBaixo();

        assertEquals(2, produtoEstoqueBaixo.size());
    }

    // ========== TESTE DE EXCEÇÕES ==========
    @Test
    void naoDeveRegistrarProdutoInexistente() {
        assertThrows(
                ProdutoInexistenteException.class,
                () -> produtoService.registrarEntrada(999L, new EntradaProdutoRequestDTO("teste", 10.00))
        );
    }

    @Test
    void naoDeveRegistrarProdutoQuantidadeInvalida() {
        // ====== ARRANGE ======
        Produto produto = criarProduto("feijao", 2);

        // ====== ACT & ASSERT ======
        assertThrows(QuantidadeInvalidaException.class,
                () -> produtoService.registrarEntrada(produto.getId(), new EntradaProdutoRequestDTO("Teste", -10))
                );
    }

}
