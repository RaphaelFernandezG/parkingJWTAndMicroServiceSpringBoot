package com.springboot.parkingjwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("me trae del response {}", request.getHeader(AUTHORIZATION));

        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")){
            filterChain.doFilter(request, response);
        }else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            log.error("Error authorization header in: {}", authorizationHeader);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);

                    log.info(decodedJWT.getPayload());

                    String username = decodedJWT.getSubject();
                    log.info("los username obtenidos del decodedJWT.getSubject(nameRole) el else de doFilterInternal en Custom Authorization {}",username);
                    String roles = decodedJWT.getClaim("nameRole").asString();
                    log.info("los roles obtenidos del decodedJWT.getClaim(nameRole) el else de doFilterInternal en Custom Authorization {}",roles);

                    String[] rolesArray= roles.split(",");
                    log.info("la cadena de roles array el else de doFilterInternal en la clase CustomAuthorization {}",rolesArray);
                    //.asArray(String.class); //revisar la key en la pagina de JWT.io la cual nos da el token
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(rolesArray).forEach(role ->{
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    log.info("las authorities en el else de doFilterInternal en la clase CustomAuthorization {}",authorities);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    log.info("el authenticationToken creado en el else de doFilterInternal en Custom Authorization {}",authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);



                }catch (Exception exception){
                    exception.printStackTrace();
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", exception.getMessage() );
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),error);

                }

            }else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
