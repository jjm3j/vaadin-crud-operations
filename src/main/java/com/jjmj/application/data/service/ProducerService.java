package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.Producer;
import com.jjmj.application.data.repository.ProducerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService extends AbstractService<Producer> {
    private final ProducerRepository producerRepository;

    protected ProducerService(ProducerRepository repository) {
        super(repository);
        this.producerRepository = repository;
    }

    public List<Producer> findAllTypes() {
        return producerRepository.findAll();
    }
}