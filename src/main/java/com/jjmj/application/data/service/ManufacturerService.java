package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.Manufacturer;
import com.jjmj.application.data.repository.ManufacturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService extends AbstractService<Manufacturer> {
    ManufacturerRepository manufacturerRepository;

    protected ManufacturerService(ManufacturerRepository repository) {
        super(repository);
        manufacturerRepository = repository;
    }

    public List<Manufacturer> findAllManufacturers() {
        return manufacturerRepository.findAll();
    }
}