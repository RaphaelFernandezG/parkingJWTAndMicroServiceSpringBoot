package com.springboot.parkingjwt.parkingrepository;

import com.springboot.parkingjwt.parkingentity.Vehicle;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByPlaca(String placa);

    @Query("SELECT COUNT(placa) FROM Vehicle")
    int countVehicle();
}
