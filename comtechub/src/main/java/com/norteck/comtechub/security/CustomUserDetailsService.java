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

public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public CustomUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.findByLogin(login);
        System.out.println("Tentando autenticar usuário: " + login);

        String role = usuario.getUsuarioComunidades().isEmpty()
                ? "MEMBRO"
                : usuario.getUsuarioComunidades().get(0).getRoleNaComunidade().name();

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(role)
                .build();
    }
}
