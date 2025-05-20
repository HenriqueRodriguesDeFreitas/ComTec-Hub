package com.norteck.comtechub.security;

import com.norteck.comtechub.model.Usuario;
import com.norteck.comtechub.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final UsuarioService usuarioService;

    public SecurityService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /*public Usuario obterUsuarioAutenticado(){
        System.out.println("A1");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Classe da autenticação: " + (authentication != null ? authentication.getClass().getName() : "null"));

        System.out.println("A2");

        if(authentication instanceof CustomAuthentication customAuth){
            System.out.println("A3");
            return customAuth.getUsuario();
        }
        System.out.println("A4");
        return null;
    }*/
    public Usuario obterUsuarioAutenticado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       if(authentication == null){
           return null;
       }

       Object principal = authentication.getPrincipal();

       if(principal instanceof  Usuario){
           return (Usuario) principal;
       }else if(principal instanceof User){
           String login = ((User) principal).getUsername();
       return  usuarioService.findByLogin(login);
       }
        return null;
    }
}
