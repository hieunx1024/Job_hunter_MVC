package com.hieu.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieu.jobhunter.domain.User;


@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    User  getUserByEmail(String email);
}
