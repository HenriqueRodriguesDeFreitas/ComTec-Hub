package com.norteck.comtechub.security;

import com.norteck.comtechub.exceptions.custom.ConflictException;
import com.norteck.comtechub.exceptions.custom.EntityNotFoundException;
import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.model.UsuarioComunidade;
import com.norteck.comtechub.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
         Usuario usuario = usuarioRepository.findByLogin(login)
                 .orElseThrow(()-> new EntityNotFoundException("Usuario não encontrado"));

         List<String> authorities = getStrings(usuario);

            return User.builder()
                    .username(usuario.getLogin())
                    .password(usuario.getSenha())
                    .authorities(authorities.toArray(new String[0]))
                    .build();

        } catch (Exception e) {
            System.err.println("Erro grave no loadUserByUsername: " + e.getMessage());
            throw new UsernameNotFoundException("Falha ao carregar usuário", e);
        }
    }

    private static List<String> getStrings(Usuario usuario) {
        List<String> authorities = new ArrayList<>();
        if (usuario.getRole() != null) {
            String rolePrincipal = "ROLE_" + usuario.getRole().name();
            authorities.add(rolePrincipal);
        }

        // 2. Roles das comunidades
        if (usuario.getUsuarioComunidades() != null) {

            for (UsuarioComunidade uc : usuario.getUsuarioComunidades()) {
                try {
                    if (uc != null && uc.getRoleNaComunidade() != null) {
                        String roleComunidade = "ROLE_" + uc.getRoleNaComunidade().name();
                        authorities.add(roleComunidade);
                    }
                } catch (Exception e) {
                    throw new ConflictException("Erro ao processar as comunidade.");
                }
            }
        }
        return authorities;
    }
}
