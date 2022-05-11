package com.springboot.parkingjwt.parkingentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationVehicles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String vehiclePlaca;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaIngreso;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaSalida;

    public RegistrationVehicles(Vehicle vehicle) {
        this.vehiclePlaca = vehicle.getPlaca();
        this.fechaIngreso = vehicle.getFechaIngreso();
        this.fechaSalida = LocalDateTime.now();
    }
}
