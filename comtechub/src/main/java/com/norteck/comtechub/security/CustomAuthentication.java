package com.norteck.comtechub.security;

import com.norteck.comtechub.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomAuthentication implements Authentication {

    private final Usuario usuario;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomAuthentication(Usuario usuario) {
        this.usuario = usuario;
        this.authorities = buildAuthorities(usuario);
    }

    private Collection<? extends GrantedAuthority> buildAuthorities(Usuario usuario) {
        List<GrantedAuthority> auths = new ArrayList<>();
        System.out.println("t1");
        if (usuario.getRole() != null) {
            System.out.println("t2");
            auths.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
        }
        System.out.println("t3");
        if (usuario.getUsuarioComunidades() != null) {
            System.out.println("t4");
            for (var uc : usuario.getUsuarioComunidades()) {
                System.out.println("t5");
                auths.add(new SimpleGrantedAuthority("ROLE_" + uc.getRoleNaComunidade().name()));
            }
            System.out.println("t6");
        }
        System.out.println("t7");
        return auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return usuario;
    }

    @Override
    public Object getPrincipal() {
        return usuario;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return usuario.getLogin();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public boolean implies(Subject subject) {
        return Authentication.super.implies(subject);
    }
}
