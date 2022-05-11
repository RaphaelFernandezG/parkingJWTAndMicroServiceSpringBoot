package com.springboot.parkingjwt.parkingrepository;

import com.springboot.parkingjwt.parkingentity.RegistrationVehicles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationVehicleRepository extends JpaRepository<RegistrationVehicles, Long> {
}
