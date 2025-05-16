package com.norteck.comtechub.repository;

import com.norteck.comtechub.model.Comunidade;
import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.model.enums.TipoComunidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComunidadeRepository extends JpaRepository<Comunidade, UUID> {
    Optional<Comunidade> findByNome(String nome);
    List<Comunidade> findByNomeContaining(String nome);
    List<Comunidade> findByTipoComunidade(TipoComunidade tipoComunidade);
    List<Comunidade> findByDescricaoContaining(String descricao);
    List<Comunidade> findByUsuarioComunidadeUsuario(Usuario usuario);
}
