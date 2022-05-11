package com.springboot.parkingjwt.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {


    //private Long emailId;
    private String email;
    private String placa;
    private String mensaje;
    //private String jeje;

}
