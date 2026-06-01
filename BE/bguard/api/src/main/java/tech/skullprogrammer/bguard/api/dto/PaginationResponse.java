package tech.skullprogrammer.bguard.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private int numberOfElements;
    private int totalPages;
    private int totalElements;

}
