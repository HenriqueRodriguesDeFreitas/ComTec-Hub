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

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "comunidade_id")
    private Comunidade comunidade;

    @Enumerated(EnumType.STRING)
    private RoleNaComunidade roleNaComunidade;
}
