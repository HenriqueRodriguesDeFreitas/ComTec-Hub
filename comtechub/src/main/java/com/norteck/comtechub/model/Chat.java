package com.norteck.comtechub.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_chat")
public class Chat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "chat")
    private List<Mensagem> mensagens;

    @OneToOne(mappedBy = "chat")
    private Comunidade comunidade;

    public Chat(){}

    public Chat(List<Mensagem> mensagens, Comunidade comunidade) {
        this.mensagens = mensagens;
        this.comunidade = comunidade;
    }

    public UUID getId() {
        return id;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }

    public Comunidade getComunidade() {
        return comunidade;
    }

    public void setComunidade(Comunidade comunidade) {
        this.comunidade = comunidade;
    }
}
