package com.hieu.jobhunter.repository;

import com.hieu.jobhunter.domain.Blog;
import com.hieu.jobhunter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByAuthor(User author);
}
