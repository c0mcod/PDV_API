package com.pdv.lalapan.entities;

import jakarta.persistence.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String username;

    private Boolean ativo;

    protected Usuario() {}

    public Usuario(String nome, String username, Boolean ativo) {
        this.nome = nome;
        this.username = username;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    private void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    private void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public void atualizarUsuario(String nome, String username, Boolean ativo) {
        // TODO: Adicionar validações
        this.nome = nome;
        this.username = username;
        this.ativo = ativo;
    }

    public void desativarUser() {
        this.ativo = false;
    }

    public void ativarUser() {
        this.ativo = true;
    }
}
