package com.cosmas.orientationapp.service;

import com.cosmas.model.Users;
import com.cosmas.orientationapp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UserRepo userRepo;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UsersService(UserRepo userRepo, AuthenticationManager authenticationManager, JWTService jwtService){
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }



    public Users register(Users users) {
        users.setPassword(encoder.encode(users.getPassword()));
        return userRepo.save(users);
    }

    public String verify(Users users) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(users.getUsername());
        }
        return "failed";
    }
}
