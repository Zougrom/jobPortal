package com.grpPfa.jobportal.controller;

import com.grpPfa.jobportal.entity.JobPostActivity;
import com.grpPfa.jobportal.entity.RecruiterJobsDto;
import com.grpPfa.jobportal.entity.RecruiterProfile;
import com.grpPfa.jobportal.entity.Users;
import com.grpPfa.jobportal.services.JobPostActivityService;
import com.grpPfa.jobportal.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@Controller
public class JobPostActivityController {
    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;
    @Autowired
    public JobPostActivityController(UsersService usersService ,JobPostActivityService jobPostActivityService){
        this.usersService= usersService;
        this.jobPostActivityService=jobPostActivityService;
        ;
    }
    @GetMapping("/dashboard/")
    public String searchJobs(Model model){
        Object currentUser=usersService.getCurrentUser();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName=authentication.getName();
            model.addAttribute("username",currentUserName);
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                List<RecruiterJobsDto>recruiterJobs=jobPostActivityService.getRecruiterJobs(((RecruiterProfile)currentUser).getUserAccountId());
                model.addAttribute("jobPost",recruiterJobs);
            }
        }
        model.addAttribute("user",currentUser);
        return "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model){
        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user",usersService.getCurrentUser());
        return "add-jobs";
    }
    @PostMapping("/dashboard/addNew")
     public String addNew(JobPostActivity jobPostActivity,Model model){

        Users users=usersService.getCurrentUserProfile();
        if(users!=null){
            jobPostActivity.setPostedById(users);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPosActivity",jobPostActivity);
       JobPostActivity saved= jobPostActivityService.addNew(jobPostActivity);
        return "redirect:/dashboard/";
     }
}
