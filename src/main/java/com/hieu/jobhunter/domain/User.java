 package com.hieu.jobhunter.domain;
 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(unique=true, nullable=false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    private String fullName;
    private String phone;

    public enum Role { ADMIN, EMPLOYER, CANDIDATE }

    // Quan hệ
    @OneToMany(mappedBy = "employer")
    private List<Job> jobs; // Chỉ EMPLOYER

    @OneToMany(mappedBy = "candidate")
    private List<Application> applications; // Chỉ CANDIDATE

    // getters/setters
}
