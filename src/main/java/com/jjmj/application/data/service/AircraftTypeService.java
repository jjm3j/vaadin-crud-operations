package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.AircraftType;
import com.jjmj.application.data.repository.AircraftTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AircraftTypeService extends AbstractService<AircraftType> {
    private final AircraftTypeRepository aircraftTypeRepository;

    protected AircraftTypeService(AircraftTypeRepository repository) {
        super(repository);
        this.aircraftTypeRepository = repository;
    }

    public List<AircraftType> findAllTypes() {
        return aircraftTypeRepository.findAll();
    }
}