package tech.skullprogrammer.bguard.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {
    @NotBlank
    private String externalCode;
    @NotBlank
    private String name;
    @NotBlank
    private String taxCode;
    @NotBlank
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
