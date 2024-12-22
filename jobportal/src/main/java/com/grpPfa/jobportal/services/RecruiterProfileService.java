package com.grpPfa.jobportal.services;

import com.grpPfa.jobportal.entity.RecruiterProfile;
import com.grpPfa.jobportal.repository.RecruiterProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {
    private final RecruiterProfileRepo recruiterProfileRepo;
    @Autowired
    public RecruiterProfileService(RecruiterProfileRepo recruiterProfileRepo){
        this.recruiterProfileRepo=recruiterProfileRepo;
    }

    public Optional<RecruiterProfile> getOne(Integer id){
      return   recruiterProfileRepo.findById(id);
    }

    public RecruiterProfile addNew(RecruiterProfile recruiterprofile) {
        return recruiterProfileRepo.save(recruiterprofile);
    }
}
