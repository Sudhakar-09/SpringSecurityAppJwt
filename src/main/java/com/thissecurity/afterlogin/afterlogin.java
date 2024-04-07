package com.thissecurity.afterlogin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth/loggedin")
public class afterlogin {
   
	@GetMapping("")
public ResponseEntity<String> loginSucess(){
    return ResponseEntity.ok("Hello WELCOME you are Securely Logged In");
}
}
