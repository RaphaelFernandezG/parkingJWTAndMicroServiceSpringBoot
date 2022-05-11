package com.springboot.parkingjwt.parkingservice;

import com.springboot.parkingjwt.parkingentity.Role;
import com.springboot.parkingjwt.parkingrepository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @ResponseStatus
    public void addNewRole(Role role) {
        roleRepository.save(role);
        //throw new ResponseStatusException(HttpStatus.CREATED, "New Rol User Has Been Registered");
    }
}
