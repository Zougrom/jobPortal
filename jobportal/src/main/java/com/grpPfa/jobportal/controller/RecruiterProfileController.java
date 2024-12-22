package com.grpPfa.jobportal.controller;

import com.grpPfa.jobportal.entity.RecruiterProfile;
import com.grpPfa.jobportal.entity.Users;
import com.grpPfa.jobportal.repository.UsersRepository;
import com.grpPfa.jobportal.services.RecruiterProfileService;
import com.grpPfa.jobportal.util.FileUploadUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
public class RecruiterProfileController {

    private final UsersRepository usersRepository;
    private final RecruiterProfileService recruiterProfileService;
    @Autowired
    public RecruiterProfileController(UsersRepository usersRepository ,RecruiterProfileService recruiterProfileService){
        this.usersRepository=usersRepository;
        this.recruiterProfileService=recruiterProfileService;
    }
    @GetMapping("/recruiter-profile/")
    public String RecruiterProfile(Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if((!(authentication instanceof AnonymousAuthenticationToken))){
            String userName=authentication.getName();
          Users users=  usersRepository.findByEmail(userName).orElseThrow(()->new UsernameNotFoundException("user not found"));
            Optional<RecruiterProfile> recruiterProfile=recruiterProfileService.getOne(users.getUserId());
            recruiterProfile.ifPresent(profile -> model.addAttribute("profile", profile));
        }
        return "recruiter_profile";
    }
    @PostMapping("/recruiter-profile/addNew")
     public String addNew(Model model, @RequestParam("image") MultipartFile multipartFile, RecruiterProfile recruiterprofile){

       Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            Users users=usersRepository.findByEmail(currentUsername).orElseThrow(()->
                    new UsernameNotFoundException("user was not found "));
            recruiterprofile.setUserId(users);
            recruiterprofile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile",recruiterprofile);
        String fileName="";
        if(!multipartFile.getOriginalFilename().equals("")){
            fileName= StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterprofile.setProfilePhoto(fileName);
        }
        RecruiterProfile savedUser=recruiterProfileService.addNew(recruiterprofile);
        String uploadDir="photos/recruiter/"+savedUser.getUserAccountId();
        try {
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return "redirect:/dashboard/";
    }

}
