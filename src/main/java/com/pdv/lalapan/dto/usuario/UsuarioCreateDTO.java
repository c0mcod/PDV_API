package com.pdv.lalapan.dto.usuario;

import com.pdv.lalapan.enums.Role;

public record UsuarioCreateDTO(
        String nome,
        Role role,
        String password
) {
}
