package com.jjmj.application.data.entity;

import com.jjmj.application.data.entity.Manufacturer;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "airships")
@Getter
@Setter
public class Airship extends AbstractEntity{
    @Column(name = "model")
    private String model;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @Column(name = "price")
    private Integer price;

    @Column(name = "year_of_manufacture")
    private int yearOfManufacture;

    @Column(name = "max_speed")
    private int maxSpeed;
}
