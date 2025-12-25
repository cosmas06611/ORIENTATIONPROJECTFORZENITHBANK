package com.cosmas.orientationapp.controller;


import com.cosmas.model.Users;
import com.cosmas.orientationapp.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService){
        this.usersService = usersService;
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users users) {
        Users user = usersService.register(users);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users users){
        return usersService.verify(users);
    }
}