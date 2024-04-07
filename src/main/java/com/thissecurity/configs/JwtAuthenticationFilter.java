package com.thissecurity.configs;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.thissecurity.configs.JwtService;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor     // TO create constructor for Final Fileds..

public class JwtAuthenticationFilter extends OncePerRequestFilter{


    private final JwtService jwtService;
    
    // Spring frame - work Inbuilt UserDetailService Interface - we need to do custom implementation in Application Config...
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal( @NonNull HttpServletRequest request,  // Http Request coming from web
    @NonNull HttpServletResponse response,   // Response that need to be sent from Backend 
    @NonNull FilterChain filterChain)  // pattern / filters which we can implement 
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
//       Checking jwt Token 
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
    }
  // extracting token from auth / authentication header 
    jwt = authHeader.substring(7);

    //  extracting useremail to check if user already exists or not from JwtToken
    userEmail=jwtService.extractUsername(jwt);
    // when user is not authenticated 
    if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
      // get the user from  Db & exists in db 

      UserDetails userDetails=this.userDetailsService.loadUserByUsername(userEmail);

    
      if(jwtService.isTokenVlaid(userEmail, userDetails)){
         //  Now if the username , token  is Vlaid then we have to update the security Context 
         // We have to create object  for Username & Password authentication Token when user is valid 
         UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(
            // we send below paramters to UsernamePasswordAuthenticationToken Object 
        userDetails,
        null,
        userDetails.getAuthorities()

       );
       authToken.setDetails(
          // then we enforce the detaiks to web Authentication & update security Context 
       new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    // After this we need to call our filter chain to do filetr to execute next filter-chains
    filterChain.doFilter(request, response);
  }

}