package com.grpPfa.jobportal.services;

import com.grpPfa.jobportal.entity.UsersType;
import com.grpPfa.jobportal.repository.UsersTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersTypeService {

    private final UsersTypeRepository usersTypeRepository;

    public UsersTypeService(UsersTypeRepository usersTypeRepository){
        this.usersTypeRepository=usersTypeRepository;
    }

    public List<UsersType> getAll(){
       return this.usersTypeRepository.findAll();
    }


}
