package com.norteck.comtechub.security;

import com.norteck.comtechub.exceptions.custom.EntityNotFoundException;
import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
         Usuario usuario = usuarioRepository.findByLogin(login)
                 .orElseThrow(()-> new EntityNotFoundException("Usuario não encontrado"));


         return new CustomAuthentication(usuario);

        } catch (Exception e) {
            System.err.println("Erro grave no loadUserByUsername: " + e.getMessage());
            throw new UsernameNotFoundException("Falha ao carregar usuário", e);
        }
    }

}
