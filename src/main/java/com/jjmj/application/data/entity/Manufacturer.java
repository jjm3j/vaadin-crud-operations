package com.jjmj.application.data.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "manufacturers")
@Getter
@Setter
public class Manufacturer extends AbstractEntity{
    @Column(name = "name")
    private String name;
}
