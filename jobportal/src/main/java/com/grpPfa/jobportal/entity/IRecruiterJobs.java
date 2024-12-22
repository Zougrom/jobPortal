package com.grpPfa.jobportal.entity;

public interface IRecruiterJobs {
    Long getTotalCandidates();
    int getjob_post_id();
    String getJob_title();
    int getLocationId();
    int getCompanyId();
    String getCity();
    String getState();
    String getName();
    String getCountry();
}
