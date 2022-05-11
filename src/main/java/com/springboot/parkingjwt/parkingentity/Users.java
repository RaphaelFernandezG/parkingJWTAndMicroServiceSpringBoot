package com.springboot.parkingjwt.parkingentity;

import com.springboot.parkingjwt.parkingrequest.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.Collection;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;
    @NotBlank
    @Column(unique = true)
    private String cedula;
    private String direction;
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotBlank
    private String password;
    @ManyToMany(fetch = EAGER)
    private Collection<Role> nameRole;

    /*@ManyToOne(optional = false)
    private Role nameRole;*/

    public Users(UserRequest user, Collection<Role> nameRole) {
        this.name = user.getName();
        this.cedula = user.getCedula();
        this.direction = user.getDirection();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nameRole = nameRole;
    }

    /*public Users(UserRequest request, Role nameRole) {
        this.name = request.getName();
        this.cedula = request.getCedula();
        this.direction = request.getDirection();
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.nameRole = nameRole;
    }*/
}
