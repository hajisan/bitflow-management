package com.example.estimationtool.toolbox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Spring læser configuration-classes ved applikationsopstart
public class SecurityConfig {
    // MIDLERTIDIGT, FOR AT KOBLE SPRING BOOTS LOGIN-METODE FRA I WEBBROWSER



    // ------ Alle sikkerhedsmekanismer er slået fra - kun password hashes ------

    @Bean // SecurituFilterChain definerer hvordan HTTP-anmodninger håndteres sikkert
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .csrf().disable() // Nu blokeres POST-formularer ikke
                .formLogin().disable() // Deaktiverer automatisk login-side fra Spring Security
                .httpBasic().disable() // Deaktiverer browser-popup til login
                .logout().disable(); // Deaktiverer Spring Security's logout-funktionalitet

        return http.build();
    }

    // -------------------------------- Hasher password ------------------------------------

    @Bean // Nu kan bcrypt's password-hasher indsprøjtes i UserService
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

        // PasswordEncoder: Interface i Spring Security
        // BCryptPasswordEncoder: klasse der implementerer interfacet, som vi instantierer ved brug

    }





}
