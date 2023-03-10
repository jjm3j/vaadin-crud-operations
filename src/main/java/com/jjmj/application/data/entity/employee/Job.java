package com.jjmj.application.data.entity.employee;

import com.jjmj.application.data.entity.AbstractEntity;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Job extends AbstractEntity {
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "job")
    private List<Employee> employees = new LinkedList<>();

    @Formula("(select count(c.id) from Employee c where c.job_id = id)")
    private int employeesCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public int getEmployeesCount() {
        return employeesCount;
    }
}
