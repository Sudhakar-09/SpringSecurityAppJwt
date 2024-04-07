package com.thissecurity.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

// JWT VALIDATION 

@Service
public class JwtService {

    private static final String SECRET_KEY= "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    // https://generate-random.org/encryption-key-generator

    // Method to extarct the Username / Email 
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Generic Method that will exact all keys as a single key and ready to pass for usage 
    // ALL CLAIMS METHOD 
     public <T> T extractClaim(String token , Function<Claims, T> ClaimsResolver){
        // extracting all claims 
        final Claims claims = extractAllClaims(token);
        return ClaimsResolver.apply(claims);
     }

     // Methods to generate the token without extra claims (Just Tokens)
     public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
     }

     // Method to Generate the Token with extra claims like user-info 
    public String generateToken(
        Map<String,Object> extraClaims,  // This extraClaim is to Pass Authority , or any info that i want to store in my token .
        UserDetails userDetails
     ){
        return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *24))
        .signWith(getSignInKey(),SignatureAlgorithm.HS256)
        .compact();
     }


     // Method to Validate the Tokens 
     public Boolean isTokenVlaid(String token, UserDetails userDetails){
               final String username = extractUsername(token);
               return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

     }
    // Method to returns Token Expiration 
  private boolean isTokenExpired(String token) {
        // TODO Auto-generated method stub
       return extractExpiration(token).before(new Date());
    }
// Method to Check if the Token is Expired or Not
private Date extractExpiration(String token) {
    // TODO Auto-generated method stub
     return extractClaim(token, Claims::getExpiration);

}

private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())  // digitally sign the jwt , creates the signature patrt of jwt . verify'the one who cliams 
        // signin key is used with conjunction with sigin-in algorithm , sepcified in header to create sigin-in algorithm. 
        // sigin-in alorithm, key-size size will depend on  the requirment . 
        // MINIMUM REQUIRMENT FOR JWT : 256 BITS 
        // https://generate-random.org/encryption-key-generator < -------->WEBSITE 
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // To generate a Sigin-in Key which is required for  Claims 
private Key getSignInKey() {
    // TODO Auto-generated method stub
     byte keyBytes[]= Decoders.BASE64.decode(SECRET_KEY);
     return Keys.hmacShaKeyFor(keyBytes);
}

}
