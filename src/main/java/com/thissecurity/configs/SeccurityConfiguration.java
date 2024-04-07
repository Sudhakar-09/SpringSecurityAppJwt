package com.thissecurity.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SeccurityConfiguration {

// Configuring our HTTP security & Build all our Jwt etc.......
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                     // ------------Disabling CSRF VERIFICATION 
        // .csrf()
        // .disable()        depricated 
                   
                        .csrf(csrf -> csrf
                        .disable())
                //  whitelist - endpoints which does'nt need security 
                      .authorizeRequests()
                      .requestMatchers("")     // list af all patterns which i want to permit all 
                      .permitAll()

                // Requestes that's need to be Authenticated 
                        .anyRequest()
                        .authenticated()
                        .and()

            //    Session Managment  & defining the session creation Policy how we want to craete it ? ---> Stateless session 
            //    Stateless ? Means the Authentication session / session state will not be STORED .
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()

            //  Authentication Provider 
                        .authenticationProvider(authenticationProvider)

            // JWT FILTER  - we will use the .authenticationProvider(authenticationProvider) filter before jwt filter is used & then userpassword authentication  so why  .addFilterBefore() is Used...
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        

   

        return http.build();
    }
}
