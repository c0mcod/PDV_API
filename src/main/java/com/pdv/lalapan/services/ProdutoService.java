package com.pdv.lalapan.services;

import com.pdv.lalapan.entities.Produto;
import com.pdv.lalapan.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository prodRepo;

    public ProdutoService(ProdutoRepository prodRepo) {
        this.prodRepo = prodRepo;
    }

    public Produto save(Produto p) {
        return prodRepo.save(p);
    }

    public List<Produto> saveList(List<Produto> pList) {
        return prodRepo.saveAll(pList);
    }

    public List<Produto> listAll(List<Produto> pListAll) {
        return prodRepo.findAll();
    }

    public Produto update(Long id, Produto prodUpdate) {
        Produto produtoExistente = prodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));

        produtoExistente.setNome(prodUpdate.getNome());
        produtoExistente.setPreco(prodUpdate.getPreco());
        produtoExistente.setUnidade(prodUpdate.getUnidade());
        produtoExistente.setCategoria(prodUpdate.getCategoria());
        produtoExistente.setQuantidadeEstoque(prodUpdate.getQuantidadeEstoque());

        return prodRepo.save(prodUpdate);
    }

    public boolean deleteById(Long id) {
        Optional<Produto> produtoExistente = prodRepo.findById(id);
        if (produtoExistente.isPresent()) {
            prodRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Produto> findById(Long id) {
        return prodRepo.findById(id);
    }
}
