package tech.skullprogrammer.bguard.test.api.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tech.skullprogrammer.bguard.api.controller.SupplyPointController;
import tech.skullprogrammer.bguard.api.dto.ErrorResponse;
import tech.skullprogrammer.bguard.api.dto.SupplyPointRequest;
import tech.skullprogrammer.bguard.api.dto.SupplyPointResponse;
import tech.skullprogrammer.bguard.api.mapper.SupplyPointMapperImpl;
import tech.skullprogrammer.bguard.api.security.SecurityConfiguration;
import tech.skullprogrammer.bguard.api.service.JWTService;
import tech.skullprogrammer.bguard.api.service.SupplyPointService;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;
import tech.skullprogrammer.bguard.domain.enumeration.ERole;
import tech.skullprogrammer.bguard.domain.enumeration.ESupplyPointType;
import tech.skullprogrammer.bguard.test.SpringTestConfigurationMVC;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import({SupplyPointMapperImpl.class, SecurityConfiguration.class, JWTService.class})
@WebMvcTest(SupplyPointController.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = SpringTestConfigurationMVC.class)
@AutoConfigureRestTestClient
public class SupplyPointControllerTest {

    @Autowired
    private RestTestClient testClient;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;

    @MockitoBean
    private SupplyPointService supplyPointService;

    private String token;

    @BeforeEach
    public void setUp() {
        UserDetails admin = User.builder().username("admin").password(passwordEncoder.encode("admin123")).roles(ERole.ADMIN.name()).build();
        token = jwtService.generateToken(admin);
    }

    @Test
    public void testFindSupplyPointByIdOK() {
        SupplyPoint supplyPoint = new SupplyPoint();
        supplyPoint.setCity("NY");
        supplyPoint.setCode("CODE1");
        supplyPoint.setId(111L);
        given(supplyPointService.getSupplyPointById(any()))
                .willReturn(supplyPoint);
        SupplyPointResponse responseBody = testClient.get().uri("/api/supply-points/111")
                .headers(headers -> headers.setBearerAuth(token))
                .exchange().expectStatus()
                .isOk().returnResult(SupplyPointResponse.class)
                .getResponseBody();
        Assertions.assertEquals("NY", responseBody.getCity());
    }

    @Test
    public void testSaveCustomerKO() {
        SupplyPointRequest request = SupplyPointRequest.builder()
                .city("NY")
                .region("New York")
                .code("CODE1")
                .type(ESupplyPointType.ELECTRICITY)
                .customerId(111L)
                .build();
        given(supplyPointService.saveSupplyPoint(any(SupplyPointRequest.class))).willThrow(new SkullException("test error", SkullException.ErrorType.CUSTOMER_NOT_FOUND));
        ErrorResponse responseBody = testClient.post().uri("/api/supply-points").body(request)
                .headers(headers -> headers.setBearerAuth(token))
                .exchange().expectStatus()
                .isEqualTo(SkullException.ErrorType.CUSTOMER_NOT_FOUND.getHttpStatus())
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();
    }

}
