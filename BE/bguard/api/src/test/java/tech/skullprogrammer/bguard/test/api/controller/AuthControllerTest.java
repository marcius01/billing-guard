package tech.skullprogrammer.bguard.test.api.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tech.skullprogrammer.bguard.api.controller.AuthController;
import tech.skullprogrammer.bguard.api.controller.CustomerController;
import tech.skullprogrammer.bguard.api.dto.*;
import tech.skullprogrammer.bguard.api.mapper.CustomerMapper;
import tech.skullprogrammer.bguard.api.mapper.CustomerMapperImpl;
import tech.skullprogrammer.bguard.api.security.SecurityConfiguration;
import tech.skullprogrammer.bguard.api.service.CustomerService;
import tech.skullprogrammer.bguard.api.service.JWTService;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.Customer;
import tech.skullprogrammer.bguard.domain.enumeration.ERole;
import tech.skullprogrammer.bguard.test.SpringTestConfigurationMVC;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import({CustomerMapperImpl.class, SecurityConfiguration.class, JWTService.class})
@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = SpringTestConfigurationMVC.class)
@AutoConfigureRestTestClient
public class AuthControllerTest {

    @Autowired
    private RestTestClient testClient;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;

    @MockitoBean
    private CustomerService customerService;
    @MockitoBean
    private UserDetailsService userDetailsService;


    private String token;
    @BeforeEach
    public void setUp() {
        UserDetails admin = User.builder().username("admin").password(passwordEncoder.encode("admin123")).roles(ERole.ADMIN.name()).build();
        given(userDetailsService.loadUserByUsername("admin"))
                .willReturn(admin);
    }

    @Test
    public void testLoginOK(){
        LoginRequest loginRequest = LoginRequest.builder().username("admin").password("admin123").build();
        LoginResponse responseBody = testClient.post().uri("/api/auth/login")
                .body(loginRequest)
                .exchange().expectStatus()
                .isOk().returnResult(LoginResponse.class)
                .getResponseBody();
        Assertions.assertTrue(jwtService.validateToken(responseBody.getAuthToken()));
    }

    @Test
    public void testLoginKO(){
        LoginRequest loginRequest = LoginRequest.builder().username("admin").password("admin12").build();
        LoginResponse responseBody = testClient.post().uri("/api/auth/login")
                .body(loginRequest)
                .exchange().expectStatus()
                .is4xxClientError().returnResult(LoginResponse.class)
                .getResponseBody();
        Assertions.assertFalse(jwtService.validateToken(responseBody.getAuthToken()));

    }
}
