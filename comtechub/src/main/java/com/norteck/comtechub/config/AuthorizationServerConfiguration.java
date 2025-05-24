package com.norteck.comtechub.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.norteck.comtechub.security.CustomAuthentication;
import com.norteck.comtechub.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    @Bean
    @Order(1)
    public SecurityFilterChain authServerSecurityFilterChain(
            HttpSecurity http, CustomAuthenticationProvider customAuthenticationProvider) throws Exception {
        // Configuração do OAuth2 Authorization Server
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        RequestMatcher endpointsMatcher = authorizationServerConfigurer
                .getEndpointsMatcher();  // Define o matcher para os endpoints do Authorization Server

        http
                .securityMatcher(endpointsMatcher) // Aplica segurança apenas nos endpoints do OAuth2
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().authenticated() // Todas requisições devem ser autenticadas
                )

                // Desabilita CSRF para endpoints OAuth2 (necessário para tokens)
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))

                .with(authorizationServerConfigurer, cfg
                        -> cfg.oidc(Customizer.withDefaults())) // Habilita OIDC (OpenID Connect)

                // Configura o Resource Server para usar JWT
                .oauth2ResourceServer(oauth2Rs
                        -> oauth2Rs.jwt(Customizer.withDefaults()))
                .authenticationProvider(customAuthenticationProvider)
                // Habilita login via formulário (para página de login padrão)
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(60))
                .refreshTokenTimeToLive(Duration.ofMinutes(90))
                .build();
    }

    @Bean
    public ClientSettings clientSettings() {
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build();
    }

    //JWK -> Json Web Key
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        RSAKey rsaKey = gerarRSA();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    //Gerar as chaves RSA
    private RSAKey gerarRSA() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey chavePublica = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey chavePrivada = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey
                .Builder(chavePublica)
                .privateKey(chavePrivada)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    //customizando endpoint para se autenticar, obter token, autenticar token, etc.
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .tokenEndpoint("/oauth2/token")
                .tokenIntrospectionEndpoint("/oauth2/introspect")  //endpoint para pegar informações do token
                .tokenRevocationEndpoint("/oauth2/revoke") // revoga o token
                .authorizationEndpoint("/oauth2/authorize")
                .oidcUserInfoEndpoint("/oauth2/userinfo") // informações do usuario OPEN ID CONNECT
                .jwkSetEndpoint("/oauth2/jks") // obter chave publica para verificar a assinatura do token
                .oidcLogoutEndpoint("oauth2/logout") // efetuar logout
                .build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                Authentication authentication = context.getPrincipal();

                if (context.getPrincipal() instanceof CustomAuthentication) {
                    CustomAuthentication userDetails = (CustomAuthentication) authentication;

                    Set<String> listAuth = userDetails.getAuthorities()
                            .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

                    context.getClaims()
                            .claim("authorities", new ArrayList<>(listAuth))
                            .claim("email", userDetails.getUsuario().getEmail());

                } else {
                    System.out.println("Principal não é CustomAuthentication. tipo: " + context.getPrincipal());
                }
            }
        };
    }

}
