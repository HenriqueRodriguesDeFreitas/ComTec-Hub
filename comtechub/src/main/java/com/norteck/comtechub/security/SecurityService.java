package com.norteck.comtechub.security;

import com.norteck.comtechub.exceptions.custom.EntityNotFoundException;
import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final UsuarioRepository usuarioRepository;

    public SecurityService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario obterUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            return null;
        }

        System.out.println("Tipo de autenticação: " + authentication.getClass().getName());
        System.out.println("Tipo do principal: " + authentication.getPrincipal().getClass().getName());

        Object principal = authentication.getPrincipal();


        if (principal instanceof CustomAuthentication) {
            return ((CustomAuthentication) principal).getUsuario();
        }
        else if(authentication instanceof CustomAuthentication){
            return ((CustomAuthentication) authentication).getUsuario();
        }
        else if (principal instanceof String) {
            return usuarioRepository.findByLogin((String) principal)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        }
        throw new EntityNotFoundException("Tipo de autneticação não suportado");
//        if(principal instanceof Usuario) {
//            return (Usuario) principal;
//        } else if(principal instanceof User) {
//            String login = ((User) principal).getUsername();
//            return usuarioRepository.findByLogin(login)
//                    .orElseThrow(()-> new EntityNotFoundException("Falha no security service;"));
//        }
//        return null;
    }
}