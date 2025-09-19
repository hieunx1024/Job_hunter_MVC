package com.hieu.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hieu.jobhunter.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{
    
}
