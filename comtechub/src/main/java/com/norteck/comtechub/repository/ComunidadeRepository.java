package com.norteck.comtechub.repository;

import com.norteck.comtechub.model.Comunidade;
import com.norteck.comtechub.model.enums.TipoComunidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ComunidadeRepository extends JpaRepository<Comunidade, UUID> {
    List<Comunidade> findByNomeContainingIgnoreCase(String nome);
    List<Comunidade> findByTipoComunidade(TipoComunidade tipoComunidade);
    List<Comunidade> findByDescricaoIgnoreCaseContaining(String descricao);
}
