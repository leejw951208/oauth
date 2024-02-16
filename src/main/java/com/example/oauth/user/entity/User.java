package com.example.oauth.user.entity;

import com.example.oauth.converter.OneWayEncryptConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_user", indexes = {@Index(name = "idx_email", columnList = "email")})
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("이메일")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Comment("비밀번호")
    @Convert(converter = OneWayEncryptConverter.class)
    @Column(name = "password")
    private String password;

    @Comment("이름")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("소셜 로그인")
    @Column(name = "provider", nullable = false)
    private String provider;

    @Comment("등록일자")
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Comment("수정일자")
    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;
}
