package com.springboot.parkingjwt.parkingcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.parkingjwt.parkingentity.RegistrationVehicles;
import com.springboot.parkingjwt.parkingentity.Vehicle;
import com.springboot.parkingjwt.parkingrequest.VehicleRequest;
import com.springboot.parkingjwt.parkingservice.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private RestTemplate restTemplate;


    @PostMapping(path="/regVehicle", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vehicle registerNewVehicle(@Valid @RequestBody VehicleRequest request) throws JsonProcessingException{
       return vehicleService.addNewVehicleRequest(request);
    }

    @PostMapping(path = "/regExitVehicle" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Vehicle registerNewExitVehicle(@Valid @RequestBody Vehicle vehicle)throws JsonProcessingException {
        return vehicleService.regExitVehicle(vehicle);
    }

    @DeleteMapping(path = "/delVehicle" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Vehicle  deleteVehicle(@Valid @RequestBody Vehicle vehicle) throws JsonProcessingException {
        return vehicleService.delVehicle(vehicle);
    }

    @GetMapping(path = "/listVehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vehicle> listVehicles(){
        return vehicleService.listVehicle();
    }

    @GetMapping(path = "/findVehicle/{idVehicle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Vehicle> findVehicleByID(@PathVariable("idVehicle")Long idVehicle){
        return vehicleService.findVehicleById(idVehicle);
    }



    @GetMapping(path = "/listRegistrationVehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegistrationVehicles> listRegistrationVehicles(){
        return vehicleService.listRegistrationVehicles();
    }


    /*regresar los 10 vehiculos que mas veces se han registrado en el parqueadero
    la consulta anterior debe permitir un rango de fecha desde y hasta, pueden ser opcionales los 2*/
    @PostMapping(path = "/listRegistrationVehiclesCant", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String,Object>> listRegistrationVehiclesConstants(@RequestParam(required = false) Date ini, @RequestParam(required = false) Date fin){
        System.out.println("Fecha inicio "+ini+" Fecha fin: "+fin);
        return vehicleService.listRegistrationVehiclesCant(ini, fin);
    }

    /*verificar de los vehiculos parqueados cuales son por primera vez y cuales no*/
    @GetMapping(path = "/listRegistrationVehiclesFirstTime", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String,Object>> listRegistrationVehiclesFirstTime(){
        return vehicleService.listRegistrationVehiclesFirstTime();
    }

    /*@GetMapping(path = "/listRegistrationVehiclesMoreTimes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String,Object>> listRegistrationVehiclesMoreTimes(){
        return vehicleService.listRegistrationVehiclesMoreTimes();
    }*/



}
