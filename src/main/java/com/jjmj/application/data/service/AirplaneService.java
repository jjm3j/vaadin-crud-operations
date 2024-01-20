package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.Airplane;
import com.jjmj.application.data.repository.AirplaneRepository;
import org.springframework.stereotype.Service;

@Service
public class AirplaneService extends AbstractService<Airplane> {
    protected AirplaneService(AirplaneRepository repository) {
        super(repository);
    }
}