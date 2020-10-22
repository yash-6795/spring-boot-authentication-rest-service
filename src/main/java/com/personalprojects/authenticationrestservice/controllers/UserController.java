package com.personalprojects.authenticationrestservice.controllers;

import com.personalprojects.authenticationrestservice.Utils;
import com.personalprojects.authenticationrestservice.models.User;
import com.personalprojects.authenticationrestservice.repository.UserRepository;
import com.personalprojects.authenticationrestservice.service.UserDetailsImpl;
import com.personalprojects.authenticationrestservice.validation.request.LoginRequest;
import com.personalprojects.authenticationrestservice.validation.response.LoginResponse;
import com.personalprojects.authenticationrestservice.validation.response.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.personalprojects.authenticationrestservice.validation.request.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.validation.Valid;

@RequestMapping("api/user")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired UserRepository userRepository;

    @Autowired Utils utils;

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired AuthenticationManager authenticationManager;

    @Autowired Utils appUtils;

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signupRequest){
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseMessage("Error: User already exist with this username!"));
        }
        User user = new User(signupRequest.getUsername(), passwordEncoder.encode(
                signupRequest.getPassword())
        );
        try{
            userRepository.save(user);
            return ResponseEntity
                    .ok()
                    .body(new ResponseMessage("Successfully added the user"));
        }
        catch (Exception e){
            return ResponseEntity
                    .status(500)
                    .body(new ResponseMessage("Error: Something went wrong, pleae try later" + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            String jwt = appUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            logger.info(jwt);
            return ResponseEntity
                    .ok()
                    .body(new LoginResponse(jwt, userDetails.getUsername()));
        } else {
            return ResponseEntity.status(403)
                    .body(new ResponseMessage("Error:Authentication failed"));
        }
    }

    @GetMapping("/greeting")
    public ResponseEntity<?> greeting(){
        return ResponseEntity.ok()
                .body(new ResponseMessage("Greetings :)"));
    }
}
