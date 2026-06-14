package com.pdv.lalapan.dto.usuario;

import com.pdv.lalapan.entities.Usuario;
import com.pdv.lalapan.enums.Role;

public record UsuarioResponseDTO(
        Long usuarioId,
        String nome,
        Role role,
        Boolean ativo
) {
    public UsuarioResponseDTO(Usuario entity) {
        this(
                entity.getId(),
                entity.getNome(),
                entity.getRole(),
                entity.getAtivo()
        );
    }
}
