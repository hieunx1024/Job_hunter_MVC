package com.hieu.jobhunter.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    // ảnh minh họa (có thể null)
    private String imageUrl;

    @Transient
    public String getExcerpt() {
        if (content == null)
            return null;
        return content.length() > 150 ? content.substring(0, 150) + "..." : content;
    }

    private LocalDateTime createdAt = LocalDateTime.now();
}
