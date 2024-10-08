package com.jellybrains.quietspace.common_service.websocket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jellybrains.quietspace.common_service.enums.StatusType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRepresentation {

    private String userId;
    private String username;
    private String firstname;
    private String lastname;
    private StatusType statusType;
    private OffsetDateTime dateOfBirth;

    @Email(message = "invalid email format")
    @NotEmpty(message = "email can not be empty")
    @NotNull(message = "email is required")
    @Size(min = 1, max = 32)
    private String email;

}
