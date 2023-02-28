package com.jjmj.application.data.repository;

import com.jjmj.application.data.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends AbstractRepository<Employee> {
    @Query("select c from Employee c " +
            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(c.position) like lower(concat('%', :searchTerm, '%') )")
    List<Employee> search(@Param("searchTerm") String searchTerm);
}