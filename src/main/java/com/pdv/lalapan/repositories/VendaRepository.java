package com.pdv.lalapan.repositories;

import com.pdv.lalapan.entities.Venda;
import com.pdv.lalapan.enums.StatusVenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long>{
    Optional<Venda> findByStatus(StatusVenda status);
}
