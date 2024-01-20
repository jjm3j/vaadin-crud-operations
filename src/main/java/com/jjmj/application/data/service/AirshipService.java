package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.Airship;
import com.jjmj.application.data.repository.AirshipRepository;
import org.springframework.stereotype.Service;

@Service
public class AirshipService extends AbstractService<Airship> {
    protected AirshipService(AirshipRepository repository) {
        super(repository);
    }
}