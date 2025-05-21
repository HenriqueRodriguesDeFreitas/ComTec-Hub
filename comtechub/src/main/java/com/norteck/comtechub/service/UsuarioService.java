package com.norteck.comtechub.service;

import com.norteck.comtechub.dto.response.ComunidadeResponseDTO;
import com.norteck.comtechub.exceptions.custom.ConflictException;
import com.norteck.comtechub.exceptions.custom.EntityNotFoundException;
import com.norteck.comtechub.mapper.ComunidadeMapper;
import com.norteck.comtechub.model.Comunidade;
import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.model.enums.RoleUsuario;
import com.norteck.comtechub.repository.UsuarioComunidadeRepository;
import com.norteck.comtechub.repository.UsuarioRepository;
import com.norteck.comtechub.security.SecurityService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;
    private final ComunidadeMapper comunidadeMapper;
    private final UsuarioComunidadeRepository usuarioComunidadeRepository;
    private final SecurityService securityService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encoder,
                          ComunidadeMapper comunidadeMapper,
                          UsuarioComunidadeRepository usuarioComunidadeRepository,
                          SecurityService securityService) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
        this.comunidadeMapper = comunidadeMapper;
        this.usuarioComunidadeRepository = usuarioComunidadeRepository;
        this.securityService = securityService;
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        verificarEmailExiste(usuario);
        verificarLoginExiste(usuario);

        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(usuario.getLogin());
        novoUsuario.setEmail(usuario.getEmail());
        novoUsuario.setRole(RoleUsuario.CADASTRADO);
        novoUsuario.setSenha(encoder.encode(usuario.getSenha()));

        return usuarioRepository.save(novoUsuario);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado."));
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email não encontrado."));
    }

    public Usuario findByLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
    }

    public List<ComunidadeResponseDTO> findComunidadesDoUsuario() {
        var usuario = securityService.obterUsuarioAutenticado();
        if (usuario == null) {
            throw new EntityNotFoundException("Usuário não autenticado");
        }

        List<Comunidade> comunidades = usuarioComunidadeRepository.findByUsuario(usuario)
                .stream()
                .map(s -> new Comunidade(s.getComunidade().getNome(),
                        s.getComunidade().getDescricao(), s.getComunidade().getCodigoAcesso(),
                        s.getComunidade().getTipoComunidade(), s.getComunidade().getChat(),
                        s.getComunidade().getFeed(), s.getComunidade().getUsuarioComunidade())).toList();


        return comunidadeMapper.comunidadesToComunidadeDto(comunidades);
    }

    @Transactional
    public Usuario update(Usuario novoUsuario) {
        Usuario usuario = securityService.obterUsuarioAutenticado();

        verificarLoginExiste(novoUsuario);
        verificarEmailExiste(novoUsuario);

        usuario.setEmail(novoUsuario.getEmail());
        usuario.setLogin(novoUsuario.getLogin());
        usuario.setSenha(novoUsuario.getSenha());

        return usuarioRepository.save(usuario);
    }

    public void deleteById() {
        Usuario usuario = securityService.obterUsuarioAutenticado();
        if (usuario.equals(null)) {
            throw new EntityNotFoundException("Usuario não autenticado.");
        }
        usuarioRepository.deleteById(usuario.getId());
    }

    private void verificarEmailExiste(Usuario usuario) {
        usuarioRepository.findByEmail(usuario.getEmail())
                .ifPresent(u -> {
                    throw new ConflictException("Email já cadastrado.");
                });
    }

    private void verificarLoginExiste(Usuario usuario) {
        usuarioRepository.findByLogin(usuario.getLogin())
                .ifPresent(u -> {
                    throw new ConflictException("Username já utilizado.");
                });
    }

}
