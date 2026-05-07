package tech.skullprogrammer.bguard.domain.customer;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Customer {

    private String id;
    private String externalCode;
    private String name;
    private String taxCode;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
