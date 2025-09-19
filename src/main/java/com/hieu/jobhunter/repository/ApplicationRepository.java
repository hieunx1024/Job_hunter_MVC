package com.hieu.jobhunter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieu.jobhunter.domain.Application;
import com.hieu.jobhunter.domain.Job;
import com.hieu.jobhunter.domain.User;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long>{
     List<Application> findByCandidate(User candidate);
     Optional<Application> findByIdAndCandidate(Long id, User candidate);
     boolean existsByJobAndCandidate(Job job, User candidate);
}
