package com.jwt.example.services;

import com.jwt.example.entities.Client;
import com.jwt.example.repositories.ClientRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomClientDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;

    public CustomClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("CLints not found !!"));
        return client;
    }
}
