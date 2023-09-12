package com.jwt.example.services;
import com.jwt.example.entities.Client;
import com.jwt.example.repositories.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Client> getClients() {

        return clientRepository.findAll();
    }

    public Client createClient(Client client) {
        client.setClientId(UUID.randomUUID().toString());
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(client);
    }
}
