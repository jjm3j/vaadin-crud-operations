package com.jjmj.application.data.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "aircrafts")
@Getter
@Setter
public class Airplane extends AbstractEntity {
    @Column(name = "model")
    private String model;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private AircraftType aircraftType;

    @ManyToOne
    @JoinColumn(name = "fuel_id")
    private FuelType fuelType;

    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @Column(name = "price")
    private Integer price;

    @Column(name = "year_of_manufacture")
    private int yearOfManufacture;

    @Column(name = "max_speed")
    private int maxSpeed;
}
