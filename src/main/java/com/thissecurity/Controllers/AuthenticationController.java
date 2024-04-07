package com.thissecurity.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thissecurity.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @GetMapping("/Register")
   public ResponseEntity<AuthenticationResponse> register(
    @RequestBody RegisterRequest request
   ){
 return  ResponseEntity.ok(service.register(request));
   }
    
// 


@GetMapping("/authenticate")
public ResponseEntity<AuthenticationResponse> authenticate(
 @RequestBody AuthenticationRequest request
){
    return  ResponseEntity.ok(service.authenticate(request));
}
}
