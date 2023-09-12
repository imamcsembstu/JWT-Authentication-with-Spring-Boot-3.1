package com.jwt.example.services;


import com.jwt.example.entities.Client;
import com.jwt.example.entities.RefreshToken;
import com.jwt.example.repositories.ClientRepository;
import com.jwt.example.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    public long refreshTokenValidity = 60 * 1000;

    private final RefreshTokenRepository refreshTokenRepository;
    private final ClientRepository clientRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, ClientRepository clientRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.clientRepository = clientRepository;
    }

    public RefreshToken createRefreshToken(String clientName) {

        Client client = clientRepository.findByEmail(clientName).get();

        RefreshToken refreshToken1 = client.getRefreshToken();
        if (refreshToken1 == null) {

           refreshToken1 = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .client(client)
                    .build();

        } else {
            refreshToken1.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        client.setRefreshToken(refreshToken1);
        refreshTokenRepository.save(refreshToken1);

        return refreshToken1;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {

        RefreshToken refreshTokenObj = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Given Token does not exist in db !!"));
        if (refreshTokenObj.getExpiry().compareTo(Instant.now()) < 0) {

            refreshTokenRepository.delete(refreshTokenObj);
            throw new RuntimeException(("Refresh token Expired !!"));
        }
        return refreshTokenObj;

    }

}
