package com.boersenparty.v_1_1.security;


import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    public String issuerUri;

    @Value("${application.client}")
    public String clientName;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.cors(Customizer.withDefaults());

        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/parties/guests/orders").permitAll()
                        .requestMatchers(HttpMethod.POST, "/parties/rooms").permitAll()
                        .requestMatchers("/price-update/**").permitAll()
                        .requestMatchers("/parties/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //.anyRequest().authenticated()
        )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                        .decoder(JwtDecoders.fromIssuerLocation(issuerUri))
                        .jwtAuthenticationConverter(customJwtAuthencationConverter()))
                );
        http.addFilterBefore(new PermitAllFilter("/parties/**"), BearerTokenAuthenticationFilter.class);

        http.sessionManagement(sessionAuthenticationStrategy -> sessionAuthenticationStrategy
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("https://dev.stock-party.live", "http://localhost:4200"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    
    @Bean
    public JwtAuthenticationConverter customJwtAuthencationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(customJwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> customJwtGrantedAuthoritiesConverter() {
        return new CustomJwtGrantedAuthoritiesConverter(clientName);
    }

}
