package com.springboot.parkingjwt.parkingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.parkingjwt.parkingentity.RegistrationVehicles;
import com.springboot.parkingjwt.parkingentity.Vehicle;
import com.springboot.parkingjwt.parkingrepository.RegistrationVehicleRepository;
import com.springboot.parkingjwt.parkingrepository.RoleRepository;
import com.springboot.parkingjwt.parkingrepository.UserRepository;
import com.springboot.parkingjwt.parkingrepository.VehicleRepository;
import com.springboot.parkingjwt.parkingrequest.VehicleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VehicleService {

    @Autowired
    private final VehicleRepository vehicleRepository;
    @Autowired
    private final RegistrationVehicleRepository registrationVehicleRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;



    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle addNewVehicleRequest(VehicleRequest request) throws JsonProcessingException {

        Optional<Vehicle> userOptional = vehicleRepository.findByPlaca(request.getPlaca());
        int cantVehicles = vehicleRepository.countVehicle();
        System.out.println("hay: "+cantVehicles+" vehiculos actualmente");
        System.out.println("Metodo Add Vehicle Request");
        if(cantVehicles<5){
            System.out.println("Hey menos de 5 vehiculos");
            if(userOptional.isPresent()){
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se puede Registrar Ingreso, ya existe la placa ");
            }
            System.out.println("Se va a registrar");
            Vehicle vehicle=new Vehicle(request.getPlaca());

            vehicleRepository.save(vehicle);
            log.info("Se registro la placa", HttpStatus.CREATED);
            System.out.println("se registro: "+vehicle);
            return vehicle;
            //throw  new ResponseStatusException(HttpStatus.CREATED,"Id: "+vehicle.getId() +" --> Id Generado Del Registro ");
        }
        log.error("Cantidad de Vehiculos maxima", HttpStatus.BAD_REQUEST);
        throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se puede Registrar, Ya se Llego a Maxima Capacidad ");


    }
    @ResponseStatus
    public Vehicle regExitVehicle(Vehicle vehicle) throws JsonProcessingException {

        System.out.println("Objeto Vehicle de registrar salida vehiculo "+ objectMapper.writeValueAsString(vehicle));

        Optional<Vehicle> vehicleOptional = vehicleRepository.findByPlaca(vehicle.getPlaca());

        System.out.println("Objeto VehicleOptional de registrar salida vehiculo "+ objectMapper.writeValueAsString(vehicleOptional));

        if(vehicleOptional.isPresent()){
            RegistrationVehicles regVehic=new RegistrationVehicles(vehicleOptional.get());

            System.out.println("Objeto registro de vehiculo de registrar salida vehiculo "+ objectMapper.writeValueAsString(regVehic));

            registrationVehicleRepository.save(regVehic);
            vehicleRepository.delete(vehicleOptional.get());
            return vehicleOptional.get();
            //throw new ResponseStatusException(HttpStatus.OK,"Salida ha Sido Registrada. Placa: "+vehicle.getPlaca());

        }
        System.out.println("Method Register Exit Vehicle");

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se Puede Registrar Salida placa, no existe la placa: "+vehicle.getPlaca());


    }

    @ResponseStatus
    public Vehicle delVehicle(Vehicle vehicle) throws JsonProcessingException {

        System.out.println(objectMapper.writeValueAsString(vehicle));

        Optional<Vehicle> vehicleOptional = vehicleRepository.findByPlaca(vehicle.getPlaca());

        if(vehicleOptional.isPresent()){
            System.out.println(objectMapper.writeValueAsString(vehicleOptional.get()));

            vehicleRepository.delete(vehicleOptional.get());
            return vehicleOptional.get();
            //throw new ResponseStatusException(HttpStatus.OK,"Se ha Eliminado el Vehiculo ");

        }
        System.out.println("Metodo Del Vehicle");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede Eliminar, no existe la placa ingresada ");


    }
    public Optional<Vehicle> findVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }
    public List<Vehicle> listVehicle() {
        return vehicleRepository.findAll();
    }

    public List<RegistrationVehicles> listRegistrationVehicles() {
        return registrationVehicleRepository.findAll();
    }


























    @ResponseStatus
    public void addVehicle(Vehicle vehicle){
        Optional<Vehicle> vehicleOptional = vehicleRepository.findByPlaca(vehicle.getPlaca());
        if(vehicleOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se puede Registrar Ingreso, ya existe la placa ");
        }
        System.out.println("Metodo Add Vehicle");
        vehicleRepository.save(vehicle);
        throw new ResponseStatusException(HttpStatus.CREATED,"Id: "+vehicle.getId() +" --> Id Generado Del Registro ");
    }

    @ResponseStatus
    public void delVehicleRequest(VehicleRequest request) {
        Optional<Vehicle> userOptional = vehicleRepository.findByPlaca(request.getPlaca());
        if(userOptional.isPresent()) {
            Vehicle vehicle=new Vehicle(request.getPlaca());
            vehicleRepository.delete(vehicle);
            throw new ResponseStatusException(HttpStatus.OK, "Salida Registrada");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede Registrar Salida, no existe la placa ingresada ");
    }
}
