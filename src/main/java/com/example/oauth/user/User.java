package com.example.oauth.user;

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
@Table(name = "t_user", indexes = {@Index(name = "idx_user_id", columnList = "user_id")})
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("아이디")
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Comment("비밀번호")
    @Convert(converter = OneWayEncryptConverter.class)
    @Column(name = "password", nullable = false)
    private String password;

    @Comment("이름")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("주민등록번호")
    @Convert(converter = OneWayEncryptConverter.class)
    @Column(name = "reg_no", nullable = false)
    private String regNo;

    @Comment("등록일자")
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Comment("수정일자")
    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;
}
