package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.Job;
import com.jjmj.application.data.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService extends AbstractService<Job> {
    private final JobRepository jobRepository;
    protected JobService(JobRepository repository) {
        super(repository);
        jobRepository = repository;
    }

    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }
}
