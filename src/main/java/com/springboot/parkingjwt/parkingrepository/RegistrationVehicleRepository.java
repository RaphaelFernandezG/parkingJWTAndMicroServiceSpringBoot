package com.springboot.parkingjwt.parkingrepository;

import com.springboot.parkingjwt.parkingentity.RegistrationVehicles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RegistrationVehicleRepository extends JpaRepository<RegistrationVehicles, Long> {

    @Query(value = "SELECT vehicle_placa, COUNT(vehicle_placa) AS cant FROM registration_vehicles GROUP BY vehicle_placa ORDER BY cant DESC LIMIT 10",nativeQuery = true)
    List<Map<String,Object>> listRegistrationVehiclesCant();

    @Query(value = "SELECT vehicle_placa FROM registration_vehicles WHERE fecha_ingreso>=ini AND fecha_ingreso<=fin GROUP BY vehicle_placa ORDER BY cant DESC LIMIT 10",nativeQuery = true)
    List<Map<String,Object>> listRegistrationVehiclesCantByFecha(@Param("ini") LocalDateTime ini, @Param("fin") LocalDateTime fin);

    @Query(value = "SELECT v.placa FROM vehicle AS v LEFT JOIN registration_vehicles AS r ON v.placa = r.vehicle_placa WHERE r.vehicle_placa IS NULL",nativeQuery = true)
    List<Map<String,Object>> listRegistrationVehiclesFirstTime();

    @Query(value = "SELECT v.placa FROM vehicle AS v LEFT JOIN registration_vehicles AS r ON v.placa = r.vehicle_placa WHERE r.vehicle_placa IS NOT NULL",nativeQuery = true)
    List<Map<String,Object>> listRegistrationVehiclesMoreTimes();

}
