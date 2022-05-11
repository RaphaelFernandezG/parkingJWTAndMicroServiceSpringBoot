package com.springboot.parkingjwt;

import com.springboot.parkingjwt.parkingentity.Role;
import com.springboot.parkingjwt.parkingentity.Users;
import com.springboot.parkingjwt.parkingrequest.UserRequest;
import com.springboot.parkingjwt.parkingrequest.VehicleRequest;
import com.springboot.parkingjwt.parkingservice.RoleService;
import com.springboot.parkingjwt.parkingservice.UserService;
import com.springboot.parkingjwt.parkingservice.VehicleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ParkingjwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingjwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	CommandLineRunner run(RoleService roleService, UserService userService, VehicleService vehicleService){
		return args -> {

			/*roleService.addNewRole(new Role(null,"ADMIN"));
			roleService.addNewRole(new Role(null, "USER"));

			userService.saveUser(new Users(null, "User Admin", "45685213", "cucuta", "admin@gmail.com", "admin", new ArrayList<>()));
			userService.saveUser(new Users(null, "Eddie Vedder", "9654872", "cucuta", "eddie@gmail.com", "usertest", new ArrayList<>()));
			userService.saveUser(new Users(null, "Mike Shinoda", "12457854", "cucuta", "mike@gmail.com", "usertest", new ArrayList<>()));
			userService.saveUser(new Users(null, "Miles Morales", "36542584", "cucuta", "miles@gmail.com", "usertest", new ArrayList<>()));

			userService.addRoleToUser("admin@gmail.com", "ADMIN");
			userService.addRoleToUser("eddie@gmail.com", "USER");
			userService.addRoleToUser("mike@gmail.com", "USER");
			userService.addRoleToUser("miles@gmail.com", "USER");

			vehicleService.addNewVehicleRequest(new VehicleRequest("ac4c48"));*/







			/*roleService.addNewRole(new Role(null, "ADMIN"));
			roleService.addNewRole(new Role(null, "USER"));

			UserRequest userRequest0= new UserRequest("Admin", "1095658451", "cucuta", "admin@gmail.com", "admin");
			UserRequest userRequest1= new UserRequest("Eddie Vedder", "13457852", "cucuta", "eddie@gmail.com", "usertest");
			UserRequest userRequest2= new UserRequest("Emilie Brown", "95645821", "cucuta", "emilie@gmail.com", "1234");
			UserRequest userRequest3= new UserRequest("Chris Cornell", "87542135", "cucuta", "chris@gmail.com", "1234");
			UserRequest userRequest4= new UserRequest("Janis Joplin", "1095648523", "cucuta", "janis@gmail.com", "1234");



			List<UserRequest> listReq = Arrays.asList(userRequest0, userRequest1, userRequest2, userRequest3, userRequest4);
			listReq.stream().forEach(userService::addNewUserRequest);
			userService.updateAdmin("admin");*/


		};
	}

}
