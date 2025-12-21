package com.pdv.lalapan.repositories;

import com.pdv.lalapan.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
