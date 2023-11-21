package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.Employee;
import com.jjmj.application.data.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService extends AbstractService<Employee>{
    protected EmployeeService(EmployeeRepository repository) {
        super(repository);
    }
}
