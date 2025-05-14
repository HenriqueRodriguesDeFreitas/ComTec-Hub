package com.norteck.comtechub.model;

import com.norteck.comtechub.model.enums.TipoComunidade;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_comunidade")
public class Comunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String descricao;
    private Integer codigoAcesso;

    @Enumerated(EnumType.STRING)
    private TipoComunidade tipoComunidade;

    @OneToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @OneToMany(mappedBy = "comunidade")
    private List<UsuarioComunidade> usuarioComunidade;

    public Comunidade(){}

    public Comunidade(String nome, String descricao, Integer codigoAcesso, TipoComunidade tipoComunidade, Chat chat, Feed feed, List<UsuarioComunidade> usuarioComunidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.codigoAcesso = codigoAcesso;
        this.tipoComunidade = tipoComunidade;
        this.chat = chat;
        this.feed = feed;
        this.usuarioComunidade = usuarioComunidade;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCodigoAcesso() {
        return codigoAcesso;
    }

    public void setCodigoAcesso(Integer codigoAcesso) {
        this.codigoAcesso = codigoAcesso;
    }

    public TipoComunidade getTipoComunidade() {
        return tipoComunidade;
    }

    public void setTipoComunidade(TipoComunidade tipoComunidade) {
        this.tipoComunidade = tipoComunidade;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public List<UsuarioComunidade> getUsuarioComunidade() {
        return usuarioComunidade;
    }

    public void setUsuarioComunidade(List<UsuarioComunidade> usuarioComunidade) {
        this.usuarioComunidade = usuarioComunidade;
    }
}
