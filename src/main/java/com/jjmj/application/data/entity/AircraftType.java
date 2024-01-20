package com.jjmj.application.data.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "aircraft_types")
@Getter
@Setter
public class AircraftType extends AbstractEntity {
    @Column(name = "name")
    private String name;
}
