package com.jwt.example.repositories;

import com.jwt.example.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {

    public Optional<Client> findByEmail(String email);
}
