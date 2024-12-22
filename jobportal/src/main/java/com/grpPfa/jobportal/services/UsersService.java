package com.grpPfa.jobportal.services;

import com.grpPfa.jobportal.entity.JobSeekerProfile;
import com.grpPfa.jobportal.entity.RecruiterProfile;
import com.grpPfa.jobportal.entity.Users;
import com.grpPfa.jobportal.repository.JobSeekerRepo;
import com.grpPfa.jobportal.repository.RecruiterProfileRepo;
import com.grpPfa.jobportal.repository.UsersRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final JobSeekerRepo jobSeekerRepo;
    private final RecruiterProfileRepo recruiterProfileRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService( UsersRepository usersRepository,
                         JobSeekerRepo jobSeekerRepo,
                         RecruiterProfileRepo recruiterProfileRepo,
                         PasswordEncoder passwordEncoder
    ){
        this.usersRepository=usersRepository;
        this.jobSeekerRepo=jobSeekerRepo;
        this.recruiterProfileRepo=recruiterProfileRepo;
        this.passwordEncoder=passwordEncoder;
    }

    public Users addUser(Users users){
            users.setActive(true);
            users.setRegistrationDate(new Date(System.currentTimeMillis()));
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            Users savedUser=usersRepository.save(users);
            int userTypeId=users.getUserTypeId().getUserTypeId();
            if(userTypeId==1){
                recruiterProfileRepo.save(new RecruiterProfile(savedUser));
            }
            else {
                jobSeekerRepo.save(new JobSeekerProfile(savedUser));
            }
        return savedUser;
    }

            public Optional<Users>getUserByEmail(String email){
                return usersRepository.findByEmail(email);

            }

    public Object getCurrentUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String userName=authentication.getName();
            Users users=usersRepository.findByEmail(userName).orElseThrow( ()-> new UsernameNotFoundException("could not found user"));
            int userId=users.getUserId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                return recruiterProfileRepo.findById(userId).orElse( new RecruiterProfile());
            }
            else{
                return jobSeekerRepo.findById(userId).orElse(new JobSeekerProfile());

            }
        }
            return null;
    }


    public Users getCurrentUserProfile() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String userName=authentication.getName();
            Users users=usersRepository.findByEmail(userName).orElseThrow( ()-> new UsernameNotFoundException("could not found user"));
            int userId=users.getUserId();
            return users;
        }
        return null;
    }
}
