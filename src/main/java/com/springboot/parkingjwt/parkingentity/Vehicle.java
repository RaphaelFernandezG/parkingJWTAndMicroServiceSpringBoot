package com.springboot.parkingjwt.parkingentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 6, max = 6)
    private String placa;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaIngreso;

    public Vehicle(String placa) {
        this.placa = placa;
        this.fechaIngreso = LocalDateTime.now();
    }


}
