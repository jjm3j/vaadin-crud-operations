package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.Helicopter;
import com.jjmj.application.data.repository.HelicopterRepository;
import org.springframework.stereotype.Service;

@Service
public class HelicopterService extends AbstractService<Helicopter> {
    protected HelicopterService(HelicopterRepository repository) {
        super(repository);
    }
}