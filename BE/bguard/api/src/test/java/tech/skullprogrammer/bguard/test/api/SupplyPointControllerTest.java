package tech.skullprogrammer.bguard.test.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import tech.skullprogrammer.bguard.api.controller.SupplyPointController;
import tech.skullprogrammer.bguard.api.dto.SupplyPointResponse;
import tech.skullprogrammer.bguard.api.mapper.SupplyPointMapperImpl;
import tech.skullprogrammer.bguard.api.service.SupplyPointService;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@Import(SupplyPointMapperImpl.class)
@WebMvcTest(SupplyPointController.class)
@AutoConfigureRestTestClient
public class SupplyPointControllerTest {

    @Autowired
    private RestTestClient testClient;

    @MockitoBean
    private SupplyPointService supplyPointService;

    @Test
    public void testFindSupplyPointByIdOK() {
        SupplyPoint supplyPoint = new SupplyPoint();
        supplyPoint.setCity("NY");
        supplyPoint.setCode("CODE1");
        supplyPoint.setId(111L);
        given(supplyPointService.getSupplyPointById(any()))
                .willReturn(supplyPoint);
        SupplyPointResponse responseBody = testClient.get().uri("/supply-points/111")
                .exchange().expectStatus()
                .isOk().returnResult(SupplyPointResponse.class)
                .getResponseBody();
        Assertions.assertEquals("NY", responseBody.getCity());
    }

}
