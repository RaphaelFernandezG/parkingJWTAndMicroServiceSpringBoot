package com.springboot.parkingjwt.parkingrepository;

import com.springboot.parkingjwt.parkingentity.RegistrationVehicles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface RegistrationVehicleRepository extends JpaRepository<RegistrationVehicles, Long> {


    @Query(value = "SELECT vehicle_placa, COUNT(vehicle_placa) AS cant " +
            "FROM registration_vehicles " +
            "WHERE fecha_ingreso>= :ini AND fecha_salida<=:fin " +
            "GROUP BY vehicle_placa ORDER BY cant DESC LIMIT 10",nativeQuery = true)
    List<Map<String,Object>> listRegistrationVehiclesCant(@Param("ini") Date ini, @Param("fin") Date fin);

    @Query(value = "SELECT v.placa, (CASE " +
            "WHEN r.vehicle_placa IS NULL THEN 'First Time' " +
            "ELSE 'Welcome Again' " +
            "END) AS estado " +
            "FROM vehicle AS v LEFT JOIN registration_vehicles AS r " +
            "ON v.placa = r.vehicle_placa WHERE r.vehicle_placa IS NULL OR r.vehicle_placa IS NOT NULL " +
            "GROUP BY v.placa, r.vehicle_placa",nativeQuery = true)
    List<Map<String,Object>> listRegistrationVehiclesFirstTime();

    /*@Query(value = "SELECT v.placa FROM vehicle AS v LEFT JOIN registration_vehicles AS r " +
            "ON v.placa = r.vehicle_placa WHERE r.vehicle_placa IS NOT NULL",nativeQuery = true)
    List<Map<String,Object>> listRegistrationVehiclesMoreTimes();*/

}
