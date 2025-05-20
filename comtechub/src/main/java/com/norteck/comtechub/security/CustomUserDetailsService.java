package com.norteck.comtechub.security;

import com.norteck.comtechub.exceptions.custom.ConflictException;
import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.model.UsuarioComunidade;
import com.norteck.comtechub.model.enums.RoleNaComunidade;
import com.norteck.comtechub.service.UsuarioService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public CustomUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            Usuario usuario = usuarioService.findByLogin(login);
            System.out.println("Usuário encontrado: " + usuario.getLogin());

            List<String> authorities = new ArrayList<>();

            // 1. Role principal do usuário
            if (usuario.getRole() != null) {
                String rolePrincipal = "ROLE_" + usuario.getRole().name();
                authorities.add(rolePrincipal);
                System.out.println("Role principal: " + rolePrincipal);
            }

            // 2. Roles das comunidades
            if (usuario.getUsuarioComunidades() != null) {
                System.out.println("Total de comunidades: " + usuario.getUsuarioComunidades().size());

                for (UsuarioComunidade uc : usuario.getUsuarioComunidades()) {
                    try {
                        if (uc != null && uc.getRoleNaComunidade() != null) {
                            String roleComunidade = "ROLE_" + uc.getRoleNaComunidade().name();
                            authorities.add(roleComunidade);
                            System.out.println("Role de comunidade: " + roleComunidade);
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao processar comunidade: " + e.getMessage());
                    }
                }
            }

            System.out.println("Total de authorities: " + authorities.size());
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
}
