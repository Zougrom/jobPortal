package com.grpPfa.jobportal.services;

import com.grpPfa.jobportal.entity.Users;
import com.grpPfa.jobportal.repository.UsersRepository;
import com.grpPfa.jobportal.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository){
        this.usersRepository=usersRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users=this.usersRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("could not find this user"));
        return new CustomUserDetails(users);

    }
}
