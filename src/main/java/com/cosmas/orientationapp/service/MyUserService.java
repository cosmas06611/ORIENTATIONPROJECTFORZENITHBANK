package com.cosmas.orientationapp.service;

import com.cosmas.model.UserPrincipal;
import com.cosmas.model.Users;
import com.cosmas.orientationapp.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

;

@Service
public class MyUserService implements UserDetailsService {

    private UserRepo userRepo;


    public MyUserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                    Users user1 = userRepo.findByUsername(username);
                    if(user1 == null){
                        throw new UsernameNotFoundException("USER NOT FOUND");
                    }

                    return new UserPrincipal(user1);
    }
}
