package com.norteck.comtechub.service;

import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario save(Usuario usuario){
        usuarioRepository.findByEmail(usuario.getEmail()).ifPresent(e -> {
            throw new RuntimeException("Usuario com este emal ja existe: " + e.getEmail());
        });
        usuarioRepository.findByLogin(usuario.getLogin()).ifPresent(l -> {
            throw new RuntimeException("Usuario com este login já existe: " + l.getLogin());
        });
        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(usuario.getLogin());
        novoUsuario.setEmail(usuario.getEmail());
        novoUsuario.setSenha(usuario.getSenha());
        return usuarioRepository.save(novoUsuario);
    }
}
