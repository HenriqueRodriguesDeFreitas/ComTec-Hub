package com.norteck.comtechub.service;

import com.norteck.comtechub.exceptions.custom.ConflictException;
import com.norteck.comtechub.exceptions.custom.EntityNotFoundException;
import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;


    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    public Usuario save(Usuario usuario) {
        verificarEmailOuLoginExistem(usuario);

        System.out.println("Senha ciptografada: " + encoder.encode(usuario.getSenha()));

        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(usuario.getLogin());
        novoUsuario.setEmail(usuario.getEmail());
        novoUsuario.setSenha(encoder.encode(usuario.getSenha()));

        return usuarioRepository.save(novoUsuario);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(UUID id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Usuario não encontrado"));
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email não encontrado."));
    }

    public Usuario findByLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
    }

    public Usuario update(UUID id, Usuario novoUsuario) {
        Usuario usuario = findById(id);

        verificarEmailOuLoginExistem(id, novoUsuario);

        usuario.setEmail(novoUsuario.getEmail());
        usuario.setLogin(novoUsuario.getLogin());
        usuario.setSenha(novoUsuario.getSenha());

        return usuarioRepository.save(usuario);
    }

    public void deleteById(UUID id) {
        Usuario usuario = findById(id);
        usuarioRepository.deleteById(usuario.getId());
    }

    private void verificarEmailOuLoginExistem(Usuario usuario) {

        if (usuarioRepository.findByLogin(usuario.getLogin()).isPresent()) {
            throw new ConflictException("Login já usado: " + usuario.getLogin());
        }
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new ConflictException("Email já cadastrado: " + usuario.getEmail());
        }
    }

    private void verificarEmailOuLoginExistem(UUID id, Usuario usuario) {
        usuarioRepository.findByLogin(usuario.getLogin()).ifPresent(exist -> {
            if (!exist.getId().equals(id)) {
                throw new ConflictException("Login já usado: " + usuario.getLogin());
            }
        });
        usuarioRepository.findByEmail(usuario.getEmail()).ifPresent(exist -> {
            if (!exist.getId().equals(id)) {
                throw new ConflictException("Email já cadastrado: " + usuario.getEmail());
            }
        });
    }
}
