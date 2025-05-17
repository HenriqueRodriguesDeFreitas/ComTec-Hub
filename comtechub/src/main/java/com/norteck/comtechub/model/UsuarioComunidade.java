package com.norteck.comtechub.model;

import com.norteck.comtechub.model.enums.RoleNaComunidade;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_usuario_comunidade")
public class UsuarioComunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "comunidade_id")
    private Comunidade comunidade;


    @Enumerated(EnumType.STRING)
    private RoleNaComunidade roleNaComunidade;

    public UsuarioComunidade(){}

    public UsuarioComunidade(Usuario usuario, Comunidade comunidade, RoleNaComunidade roleNaComunidade) {
        this.usuario = usuario;
        this.comunidade = comunidade;
        this.roleNaComunidade = roleNaComunidade;
    }

    public UUID getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Comunidade getComunidade() {
        return comunidade;
    }

    public void setComunidade(Comunidade comunidade) {
        this.comunidade = comunidade;
    }

    public RoleNaComunidade getRoleNaComunidade() {
        return roleNaComunidade;
    }

    public void setRoleNaComunidade(RoleNaComunidade roleNaComunidade) {
        this.roleNaComunidade = roleNaComunidade;
    }
}
