package com.grpPfa.jobportal.repository;

import com.grpPfa.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerRepo extends JpaRepository<JobSeekerProfile ,Integer> {

}
