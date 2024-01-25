package com.jjmj.application.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fuel_types")
@Getter
@Setter
public class FuelType extends AbstractEntity {
    @Column(name = "name")
    private String name;
}
