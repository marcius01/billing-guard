package tech.skullprogrammer.bguard.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tech.skullprogrammer.bguard.domain.SkullException;

import java.util.HashMap;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {

    @Getter
    private String message;
    private SkullException.ErrorType error;
    private final String prefixCode = "BGUARD";
    @Builder.Default
    @Getter
    private Map<String, String> payload = new HashMap<>();

    public String getErrorCode() {
        return prefixCode + "-" + error.name();
    }
}
