package com.jwt.example.controllers;

import com.jwt.example.dto.JwtRequest;
import com.jwt.example.dto.JwtResponse;
import com.jwt.example.dto.RefreshTokenRequest;
import com.jwt.example.entities.Client;
import com.jwt.example.entities.RefreshToken;
import com.jwt.example.security.JwtHelper;
import com.jwt.example.services.ClientService;
import com.jwt.example.services.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserDetailsService userDetailsService;

    private AuthenticationManager manager;

    private JwtHelper helper;
    private ClientService clientService;

    private  RefreshTokenService refreshTokenService;

    public AuthController(UserDetailsService userDetailsService, AuthenticationManager manager, JwtHelper helper,
                          ClientService clientService, RefreshTokenService refreshTokenService) {
        this.userDetailsService = userDetailsService;
        this.manager = manager;
        this.helper = helper;
        this.clientService = clientService;
        this.refreshTokenService = refreshTokenService;
    }
    private Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

        this.doAuthenticate(request.getEmail(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String token = this.helper.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .userName(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @PostMapping("/refresh")
    public JwtResponse refreshJwtToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());

        Client client =refreshToken.getClient();

        String generateToken = this.helper.generateToken(client);
        return JwtResponse.builder().refreshToken(refreshToken.getRefreshToken())
                .jwtToken(generateToken)
                .userName(client.getEmail())
                .build();

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @PostMapping("/create-client")
    public Client createClient(@RequestBody Client client){
        return clientService.createClient(client);
    }


}
