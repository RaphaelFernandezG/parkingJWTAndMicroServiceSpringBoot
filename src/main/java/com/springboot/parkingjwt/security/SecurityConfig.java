package com.springboot.parkingjwt.security;

import com.springboot.parkingjwt.filter.CustomAuthenticationFilter;
import com.springboot.parkingjwt.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/login/**", "/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers(GET,"/users/**").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(POST,"/users/**").hasAnyAuthority("USER");
        //http.authorizeRequests().antMatchers(POST,"**/email/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(POST,"/users/addUser").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(POST,"/users/sendEmail").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET,"/vehicle/**").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(POST,"/vehicle/**").hasAnyAuthority("USER");
        //http.authorizeRequests().anyRequest().permitAll(); //permitimos todos los usuarios con el .permitAll()
        http.authorizeRequests().anyRequest().authenticated(); //para permitir solo los tipos de usuarios authenticados anteriormente
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }


}
