package com.grpPfa.jobportal.repository;

import com.grpPfa.jobportal.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    public Optional<Users>findByEmail(String email);

}
