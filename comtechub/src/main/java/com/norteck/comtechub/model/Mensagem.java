package com.norteck.comtechub.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_mensagem")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String texto;

    @Column(nullable = false)
    private LocalDateTime dataHoraMensagem;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public Mensagem(){}

    public Mensagem(String texto, LocalDateTime dataHoraMensagem, Usuario usuario, Chat chat) {
        this.texto = texto;
        this.dataHoraMensagem = dataHoraMensagem;
        this.usuario = usuario;
        this.chat = chat;
    }

    public UUID getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataHoraMensagem() {
        return dataHoraMensagem;
    }

    public void setDataHoraMensagem(LocalDateTime dataHoraMensagem) {
        this.dataHoraMensagem = dataHoraMensagem;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
