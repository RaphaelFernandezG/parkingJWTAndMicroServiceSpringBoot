package com.springboot.parkingjwt.parkingrepository;

import com.springboot.parkingjwt.parkingentity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByCedula(String cedula);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByName(String name);

    //void addRoleToUser(String username, String roleName);



}
