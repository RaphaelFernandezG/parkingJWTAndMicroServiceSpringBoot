package com.springboot.parkingjwt.parkingservice;

import com.springboot.parkingjwt.parkingentity.Role;
import com.springboot.parkingjwt.parkingentity.Users;
import com.springboot.parkingjwt.parkingentity.Vehicle;
import com.springboot.parkingjwt.parkingrepository.RoleRepository;
import com.springboot.parkingjwt.parkingrepository.UserRepository;
import com.springboot.parkingjwt.parkingrepository.VehicleRepository;
import com.springboot.parkingjwt.VO.EmailRequest;
import com.springboot.parkingjwt.parkingrequest.UserRequest;
import com.springboot.parkingjwt.responses.GenericResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j @Primary
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final VehicleRepository vehicleRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final PasswordEncoder passwordEncoder; //Inyectamos el password encoder



    @ResponseStatus
    public Users addNewUserRequest(UserRequest request) {

        log.info("Saving new Users {} to the database", request.getName());

        /*Role role = roleRepository.findByName("USER") //.orElseThrow(() -> new IllegalStateException("Cedula is already registered "));
        //Optional<Users> userOptionalEmail = userRepository.findUserByEmail(request.getEmail());*/

        Collection<Role> role = new ArrayList<>();
        role.add(roleRepository.findByName("USER"));
        Optional<Users> userOptional = userRepository.findByCedula(request.getCedula());
        Optional<Users> userOptionalEmail = userRepository.findByEmail(request.getEmail());

        if(userOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cedula is already registered ");
        }
        if(userOptionalEmail.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email is already registered ");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Users user= new Users(request,role);
        userRepository.save(user);
        addRoleToUser(user.getEmail(),"USER");
        return user;
        //throw new ResponseStatusException(HttpStatus.ACCEPTED,"User Registered ");

    }

    @ResponseStatus
    public GenericResponse sendEmailRequest(EmailRequest emailRequest, HttpServletRequest request){

        Principal principal = request.getUserPrincipal();
        String emailAdmin = principal.getName();

        log.info("Send Email desde el UserService");
        Optional<Vehicle> vehicleOptional = vehicleRepository.findByPlaca(emailRequest.getPlaca());

        if (vehicleOptional.isPresent()){
            Optional<Users> userOptional = userRepository.findByEmail(emailRequest.getEmail());
            if(userOptional.isPresent()){
                restTemplate.postForObject("http://localhost:8081/api/email/sendEmail",emailRequest, EmailRequest.class);

                return new GenericResponse(emailAdmin + " Su correo se ha enviado satisfactoriamente");
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El Correo al cual desea enviar no se encontro registrado, verifique e intente nuevamente");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La placa a enviar correo no se encuentra registrada");
    }
    public List<Users> listUsers(){
        return userRepository.findAll();
    }

    public Users findByEmailUser(String email){
        return userRepository.findByEmail(email).get();
    }

    public void addRoleToUser(String email, String nameRole) {
        log.info("Adding Role {} to Users {}", email, nameRole);
        //Optional<Users> users = userRepository.findByEmail(email);
        //Role role = roleRepository.findByName(nameRole);
        //userRepository.addRoleToUser(users.get().getName(), role.getName());

        Optional<Users> users = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(nameRole);
        users.get().getNameRole().add(role);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> usersOptional = userRepository.findByEmail(email);
        if(!usersOptional.isPresent()){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }else{
            log.info("User found in the database: {}", email);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            usersOptional.get().getNameRole().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new org.springframework.security.core.userdetails.User(usersOptional.get().getEmail(), usersOptional.get().getPassword(), authorities);
        }

    }






















    /*@ResponseStatus
    public void addNewUser(UserRequest user) {

        /*Role role = roleRepository.findByName("USER") //.orElseThrow(() -> new IllegalStateException("Cedula is already registered "));
        //Optional<Users> userOptionalEmail = userRepository.findUserByEmail(request.getEmail());

        Collection<Role> role = new ArrayList<>();
        role.add(roleRepository.findByName("USER"));
        Optional<Users> userOptional = userRepository.findUserByCedula(user.getCedula());
        Optional<Users> userOptionalEmail = userRepository.findUserByEmail(user.getEmail());

        if(userOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cedula is already registered ");
        }
        if(userOptionalEmail.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email is already registered ");
        }
        userRepository.save(new Users(user,role));
        throw new ResponseStatusException(HttpStatus.ACCEPTED,"User Registered ");

    }*/

    @ResponseStatus
    public  void updateAdmin(String name){

        /*Optional<Users> usersOptional=userRepository.findUserByName(name);
        Optional<Role> role=roleRepository.findByName("ADMIN");*/

        Collection<Role> role = new ArrayList<>();
        role.add(roleRepository.findByName("ADMIN"));
        Optional<Users> usersOptional = userRepository.findByName(name);


        if(usersOptional.isPresent()){
            usersOptional.get().setNameRole(role);
            throw new ResponseStatusException(HttpStatus.ACCEPTED,"Admin Updated ");

        }
        throw new ResponseStatusException(HttpStatus.ACCEPTED,"User Admin is not registered ");



    }






    /*public Users saveUser(Users users) {
        log.info("Saving new Users {} to the database", users.getName());
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return userRepository.save(users);
    }*/















}
