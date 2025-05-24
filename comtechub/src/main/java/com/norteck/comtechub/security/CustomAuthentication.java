package com.norteck.comtechub.security;

import com.norteck.comtechub.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomAuthentication implements Authentication, UserDetails {

    private final Usuario usuario;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomAuthentication(Usuario usuario) {
        this.usuario = usuario;
        this.authorities = buildAuthorities(usuario);
    }

    private Collection<? extends GrantedAuthority> buildAuthorities(Usuario usuario) {
        List<GrantedAuthority> auths = new ArrayList<>();

        if (usuario.getRole() != null) {
            auths.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
        }
        if (usuario.getUsuarioComunidades() != null) {
            for (var uc : usuario.getUsuarioComunidades()) {
                auths.add(new SimpleGrantedAuthority("ROLE_" + uc.getRoleNaComunidade().name()));
            }
        }
        return auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Object getCredentials() {
        return this.getPassword();
    }

    @Override
    public Object getDetails() {
        return usuario;
    }

    @Override
    public Object getPrincipal() {
        return this;
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
