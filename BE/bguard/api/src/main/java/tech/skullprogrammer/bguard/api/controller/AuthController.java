package tech.skullprogrammer.bguard.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.skullprogrammer.bguard.api.dto.LoginRequest;
import tech.skullprogrammer.bguard.api.dto.LoginResponse;
import tech.skullprogrammer.bguard.api.service.JWTService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @PostMapping(value ="/login" )
    public LoginResponse login (@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return new LoginResponse(token);
    }
}
