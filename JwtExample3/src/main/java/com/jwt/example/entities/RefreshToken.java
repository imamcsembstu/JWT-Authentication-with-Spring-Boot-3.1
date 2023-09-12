package com.jwt.example.entities;

import jakarta.persistence.*;
import lombok.*;

import javax.naming.Name;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    private String refreshToken;
    private Instant expiry;

    @OneToOne
    private Client client;



}
