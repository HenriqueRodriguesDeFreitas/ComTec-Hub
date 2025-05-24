package com.norteck.comtechub.model;

import com.norteck.comtechub.model.enums.RoleUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_usuario")
public class Usuario implements Serializable {

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

    @Enumerated(EnumType.STRING) // Mapeia o enum como string no BD
    @Column(nullable = false)
    private RoleUsuario role; // Agora é um campo único (não lista)

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<UsuarioComunidade> usuarioComunidades;

    @OneToMany(mappedBy = "usuario")
    private List<Mensagem> mensagens;

    // Construtores
    public Usuario() {
        this.role = RoleUsuario.CADASTRADO; // Valor padrão
    }

    public Usuario(String login, String senha, String email, List<RoleUsuario> roleUsuario,
                   List<UsuarioComunidade> usuarioComunidades, List<Mensagem> mensagens) {
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.usuarioComunidades = usuarioComunidades;
        this.mensagens = mensagens;
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

    public RoleUsuario getRole() {
        return role;
    }

    public void setRole(RoleUsuario role) {
        this.role = role;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
}
