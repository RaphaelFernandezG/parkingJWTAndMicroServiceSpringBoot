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
import java.util.List;
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

    @GetMapping("/listVehicles")
    public List<Vehicle> listVehicles(){
        return vehicleService.listVehicle();
    }

    @GetMapping(path = "/findVehicle/{idVehicle}")
    public Optional<Vehicle> findVehicleByID(@PathVariable("idVehicle")Long idVehicle){
        return vehicleService.findVehicleById(idVehicle);
    }



    @GetMapping("/listRegistrationVehicles")
    public List<RegistrationVehicles> listRegistrationVehicles(){
        return vehicleService.listRegistrationVehicles();
    }



}
