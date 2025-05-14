package com.norteck.comtechub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String login;
    @Column(nullable = false)
    private String senha;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioComunidade> usuarioComunidades;

    @OneToMany(mappedBy = "usuario")
    private List<Mensagem> mensagens;

    public Usuario(){}

    public Usuario(String login, String senha, String email) {
        this.login = login;
        this.senha = senha;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UsuarioComunidade> getUsuarioComunidades() {
        return usuarioComunidades;
    }

    public void setUsuarioComunidades(List<UsuarioComunidade> usuarioComunidades) {
        this.usuarioComunidades = usuarioComunidades;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
}
