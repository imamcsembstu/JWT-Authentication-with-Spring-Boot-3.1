package com.jwt.example.config;

import com.jwt.example.security.JwtAuthenticationEntryPoint;
import com.jwt.example.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private JwtAuthenticationEntryPoint point;

    private JwtAuthenticationFilter filter;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;


    public SecurityConfig(JwtAuthenticationEntryPoint point, JwtAuthenticationFilter filter, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.point = point;
        this.filter = filter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/home/**").authenticated()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/create-client").permitAll()
                        .requestMatchers("/auth/refresh").permitAll().anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProviderObject = new DaoAuthenticationProvider();
        daoAuthenticationProviderObject.setUserDetailsService(userDetailsService);
        daoAuthenticationProviderObject.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProviderObject;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
