package com.hieu.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieu.jobhunter.domain.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByEmployerEmail(String email);

}
