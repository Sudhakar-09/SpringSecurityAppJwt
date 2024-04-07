package com.thissecurity.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thissecurity.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	 private final UserRepository userRepository;

    // @Bean
    // public UserDetailsService userDetailsService(){

    //     return new  UserDetailsService() {
    //      // get user from database 
    //         public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //             // TODO Auto-generated method stub
    //             return userRepository.findByEmail(username)
    //             .orElseThrow(new UsernameNotFoundException("User Not Found"));
    //         }
            
    //     };
    // }


    @Bean
    public UserDetailsService userDetailsService() {
      return username -> userRepository.findByEmail(username)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    //  AuthenticationProvider Bean Implemnattion for DATA ACCESS LAYER - to get user details from 

    @Bean 
    public AuthenticationProvider authenticationProvider(){
   DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
   // to fetch userdetails we need to speecify to Authprovider 
   authProvider.setUserDetailsService(userDetailsService());
   // We need to provide which passowrd encoder / Decoder we need to use we need to specify to AuthProvider ...RIGHT ALGORITHM
   authProvider.setPasswordEncoder(passwordEncoder());
   return authenticationProvider();

    }

    // AUTHENTICATION MANAGER TO AUTHENTICATE USING USERNAME & PASSWORD 
    // Default Implemenation of Spring Boot .......
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
       return  config.getAuthenticationManager();
    }

    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // TODO Auto-generated method stub
        return new BCryptPasswordEncoder();
      
    }
}
