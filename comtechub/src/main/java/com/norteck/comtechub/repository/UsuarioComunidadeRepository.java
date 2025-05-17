package com.norteck.comtechub.repository;

import com.norteck.comtechub.model.Comunidade;
import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.model.UsuarioComunidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UsuarioComunidadeRepository extends JpaRepository<UsuarioComunidade, UUID> {
    List<UsuarioComunidade> findByComunidade(Comunidade comunidade);
    List<UsuarioComunidade> findByUsuario(Usuario usuario);

}
