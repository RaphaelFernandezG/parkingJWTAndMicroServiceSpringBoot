package com.springboot.parkingjwt.parkingcontroller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.parkingjwt.parkingentity.Role;
import com.springboot.parkingjwt.parkingentity.Users;
import com.springboot.parkingjwt.VO.EmailRequest;
import com.springboot.parkingjwt.parkingrequest.UserRequest;
import com.springboot.parkingjwt.parkingservice.UserService;
import com.springboot.parkingjwt.responses.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/list")
    public ResponseEntity<List<Users>> listUsers(HttpServletRequest request){

        Principal principal = request.getUserPrincipal();
        String emailAdmin = principal.getName();
        System.out.println("el email del admin es: "+emailAdmin);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/list").toUriString());
        return ResponseEntity.created(uri).body(userService.listUsers());

    }

    @PostMapping("/addUser")
    public ResponseEntity<Users> saveUser(@Valid @RequestBody UserRequest userRequest){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/addUser").toUriString());
        return ResponseEntity.created(uri).body(userService.addNewUserRequest(userRequest));
    }


    @PostMapping("/sendEmail")
    public GenericResponse sendEmail(@Valid @RequestBody EmailRequest emailRequest, HttpServletRequest request){

        /*Principal principal = request.getUserPrincipal();
        String emailAdmin = principal.getName();*/
        //System.out.println(currentUserName((Principal) restTemplate.getRequestFactory()));

        return userService.sendEmailRequest(emailRequest, request);
    }






    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Users user = userService.findByEmailUser(username);
                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("nameRole", user.getNameRole().stream().map(Role::getName).collect(Collectors.joining()))
                        .sign(algorithm);
                //podemos mapear y mostrarlo como un JSon
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);

            }catch (Exception exception){
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage() );
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);

            }

        }else {
            throw new RuntimeException("Refresh token is missing");
        }

    }
}
