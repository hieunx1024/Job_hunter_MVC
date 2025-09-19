package com.hieu.jobhunter.config;

import com.hieu.jobhunter.domain.Category;
import com.hieu.jobhunter.domain.User;
import com.hieu.jobhunter.repository.CategoryRepository;
import com.hieu.jobhunter.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CategoryRepository categoryRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Tạo categories mẫu nếu chưa có
        if (categoryRepository.count() == 0) {
            Category tech = new Category();
            tech.setName("Technology");
            categoryRepository.save(tech);

            Category finance = new Category();
            finance.setName("Finance");
            categoryRepository.save(finance);

            Category healthcare = new Category();
            healthcare.setName("Healthcare");
            categoryRepository.save(healthcare);

            Category education = new Category();
            education.setName("Education");
            categoryRepository.save(education);

            Category marketing = new Category();
            marketing.setName("Marketing");
            categoryRepository.save(marketing);

            System.out.println("Sample categories created successfully!");
        }

        // Tạo user employer mẫu nếu chưa có
        if (userRepository.count() == 0) {
            User employer = new User();
            employer.setUsername("employer");
            employer.setEmail("employee"); // Email để login
            employer.setPassword(passwordEncoder.encode("password"));
            employer.setRole(User.Role.EMPLOYER);
            employer.setFullName("Sample Employer");
            employer.setCompanyName("Sample Company");
            userRepository.save(employer);

            System.out.println("Sample employer user created successfully!");
            System.out.println("Login with email: employee, password: password");
        }
    }
}
