package com.springboot.parkingjwt.parkingrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {


    @NotBlank(message = "se necesita la placa")
    public String placa;

    /*public VehicleRequest(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public String toString() {
        return "VehicleRequest{" +
                "placa='" + placa + '\'' +
                '}';
    }*/
}
