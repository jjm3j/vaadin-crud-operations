package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.Style;
import com.jjmj.application.data.repository.StyleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleService extends AbstractService<Style> {
    private final StyleRepository styleRepository;
    protected StyleService(StyleRepository repository) {
        super(repository);
        styleRepository = repository;
    }

    public List<Style> findAllStyles() {
        return styleRepository.findAll();
    }
}