package tech.skullprogrammer.bguard.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerResponse {
    private String externalCode;
    private String name;
    private String taxCode;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
