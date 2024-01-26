package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("select user from User user " + "where (user.role) = 'Admin'")
    List<User> getAdmins();

    @Query("select user from User user " + "where (user.login) = :name")
    User findByLogin(@Param("name") String name);
}
