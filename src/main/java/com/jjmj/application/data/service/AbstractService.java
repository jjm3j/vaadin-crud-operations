package com.jjmj.application.data.service;

import com.jjmj.application.data.entity.AbstractEntity;
import com.jjmj.application.data.repository.AbstractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class AbstractService <T extends AbstractEntity> {
    private final AbstractRepository<T> repository;

    protected AbstractService(AbstractRepository<T> repository) {
        this.repository = repository;
    }

    public T add(T entity) {
        return repository.save(entity);
    }

    public Optional<T> get(Long id) {
        return repository.findById(id);
    }

    public T update(T entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public Page<T> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /*
    public Page<T> list(Pageable pageable, Specification<T> filter) {
        return repository.findAll(filter, pageable);
    }
    */

    public List<T> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return repository.findAll();
        } else {
            return repository.search(stringFilter);
        }
    }

    public int count() {
        return (int) repository.count();
    }
}
