package com.jjmj.application.data.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "helicopters")
@Getter
@Setter
public class Helicopter extends AbstractEntity{
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
