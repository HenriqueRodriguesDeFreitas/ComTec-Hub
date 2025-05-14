package com.norteck.comtechub.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_feed")
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "feed")
    private List<Comunidade> comunidades;

    public Feed(){}

    public Feed(List<Comunidade> comunidades) {
        this.comunidades = comunidades;
    }

    public UUID getId() {
        return id;
    }

    public List<Comunidade> getComunidades() {
        return comunidades;
    }

    public void setComunidades(List<Comunidade> comunidades) {
        this.comunidades = comunidades;
    }
}
