package com.jwt.example.controllers;

import com.jwt.example.entities.Client;
import com.jwt.example.services.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    private final ClientService clientService;

    public HomeController(ClientService clientService) {
        this.clientService = clientService;
    }


    //http://localhost:8081/home/clients

    @GetMapping("/clients")
    public List<Client> getClient(){
        return this.clientService.getClients();
    }

    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal){
        return principal.getName();
    }


}
