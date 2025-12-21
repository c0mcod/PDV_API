package com.pdv.lalapan.services;

import com.pdv.lalapan.entities.Venda;
import com.pdv.lalapan.enums.StatusVenda;
import com.pdv.lalapan.repositories.VendaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VendaService {

    private final VendaRepository vendaRepo;

    public VendaService(VendaRepository vendaRepo) {
        this.vendaRepo = vendaRepo;
    }



}
