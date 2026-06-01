package tech.skullprogrammer.bguard.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PaginationForRequest {
    @PositiveOrZero()
    private int page;
    @Min(1) @Max(100)
    private int size = 10;
    private String sort = "id,desc";
}
