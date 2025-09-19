package com.hieu.jobhunter.service;

import com.hieu.jobhunter.domain.Blog;
import com.hieu.jobhunter.domain.User;
import com.hieu.jobhunter.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Blog updateBlog(Blog blog) {
        // JPA save() sẽ update nếu blog.id tồn tại
        return blogRepository.save(blog);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    public List<Blog> getBlogsByAuthor(User author) {
        return blogRepository.findByAuthor(author);
    }
}
