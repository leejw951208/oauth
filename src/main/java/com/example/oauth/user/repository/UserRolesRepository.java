package com.example.oauth.user.repository;

import com.example.oauth.user.entity.User;
import com.example.oauth.user.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
    List<UserRoles> findByUser(User user);
}
