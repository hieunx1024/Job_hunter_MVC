package com.hieu.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieu.jobhunter.domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long>{
    
}
