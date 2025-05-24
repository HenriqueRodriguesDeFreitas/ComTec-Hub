package com.norteck.comtechub.repository;

import com.norteck.comtechub.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.usuarioComunidades WHERE u.login = :login")
    Optional<Usuario> findByLogin(@Param("login") String login);

}
