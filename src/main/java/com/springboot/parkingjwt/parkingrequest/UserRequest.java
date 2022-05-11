package com.springboot.parkingjwt.parkingrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String name;
    private String cedula;
    private String direction;
    private String email;
    private String password;

}
