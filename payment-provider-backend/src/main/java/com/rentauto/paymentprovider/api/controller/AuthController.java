package com.rentauto.paymentprovider.api.controller;

import com.rentauto.paymentprovider.api.dto.LoginRequest;
import com.rentauto.paymentprovider.api.dto.RegisterRequest;
import com.rentauto.paymentprovider.api.dto.RegisterResponse;
import com.rentauto.paymentprovider.service.JwtService;
import com.rentauto.paymentprovider.service.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final MerchantService merchantService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
            var userDetails = merchantService.getByEmail(request.email());
            String token = jwtService.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", userDetails.getUsername(),
                    "role", Objects.requireNonNull(userDetails.getAuthorities().iterator().next().getAuthority())
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = merchantService.create(request);
        return ResponseEntity.ok(response);
    }
}
