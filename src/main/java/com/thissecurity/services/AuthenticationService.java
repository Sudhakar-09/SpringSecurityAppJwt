package com.thissecurity.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thissecurity.Controllers.AuthenticationRequest;
import com.thissecurity.Controllers.AuthenticationResponse;
import com.thissecurity.Controllers.RegisterRequest;
import com.thissecurity.Entities.Role;
import com.thissecurity.Entities.User;
import com.thissecurity.Repositories.UserRepository;
import com.thissecurity.configs.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationService service;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // TODO Auto-generated method stub

      var user = User.builder()
      .fullName(request.getFirstName())
      .email(request.getEmail())
      .passwd(passwordEncoder.encode(request.getPassword()))
      .role(Role.TRAINEE)
      .build();
      userRepository.save(user);

      // jwt token 
       var JwtToken = jwtService.generateToken(user);
       return AuthenticationResponse.builder()
       .token(JwtToken)
       .build();
    }


public AuthenticationResponse authenticate(AuthenticationRequest request) {
    // TODO Auto-generated method stub
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(), 
            request.getPassword())
      );

      var user= userRepository.findByEmail(request.getEmail())
      .orElseThrow();
      var JwtToken = jwtService.generateToken(user);
      return AuthenticationResponse.builder()
      .token(JwtToken)
      .build();
   
}

}
