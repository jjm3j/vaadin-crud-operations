package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.FuelType;
import com.jjmj.application.data.repository.FuelTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuelTypeService extends AbstractService<FuelType> {
    private final FuelTypeRepository aircraftTypeRepository;

    protected FuelTypeService(FuelTypeRepository repository) {
        super(repository);
        this.aircraftTypeRepository = repository;
    }

    public List<FuelType> findAllTypes() {
        return aircraftTypeRepository.findAll();
    }
}