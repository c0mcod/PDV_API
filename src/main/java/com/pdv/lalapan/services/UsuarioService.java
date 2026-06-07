package com.pdv.lalapan.services;

import com.pdv.lalapan.dto.auth.LoginDTO;
import com.pdv.lalapan.dto.usuario.UsuarioAtualizadoDTO;
import com.pdv.lalapan.dto.usuario.UsuarioCreateDTO;
import com.pdv.lalapan.dto.usuario.UsuarioResponseDTO;
import com.pdv.lalapan.entities.Usuario;
import com.pdv.lalapan.exceptions.UsuarioNaoEncontradoException;
import com.pdv.lalapan.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO createUser(UsuarioCreateDTO dto) {
        Usuario user = new Usuario(
                dto.nome(),
                dto.username(),
                passwordEncoder.encode(dto.password()),
                true
        );

        Usuario userSalvo = userRepo.save(user);
        return new UsuarioResponseDTO(userSalvo);
    }

    public UsuarioAtualizadoDTO updateUser(Long idUser, UsuarioAtualizadoDTO dto) {
        Usuario newUser = userRepo.findById(idUser)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(idUser));

        newUser.atualizarUsuario(
                dto.nome(),
                dto.username(),
                true
        );

        Usuario usuarioAtualizado = userRepo.save(newUser);
        return UsuarioAtualizadoDTO.fromEntity(usuarioAtualizado);
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario user = userRepo.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        return new UsuarioResponseDTO(user);
    }

    public String buscarNome(Long id) {
        Usuario user = userRepo.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        return user.buscarNome();
    }

    public void desativar(Long id) {
        Usuario user = userRepo.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        user.desativarUser();
        userRepo.save(user);
    }

    public void ativar(Long id) {
        Usuario user = userRepo.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        user.ativarUser();
        userRepo.save(user);
    }

    public List<UsuarioResponseDTO> listarTodosUsuarios() {
        return userRepo.findAll()
                .stream()
                .map(u -> new UsuarioResponseDTO(
                        u.getId(),
                        u.getNome(),
                        u.getUsername(),
                        u.getAtivo()
                ))
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
