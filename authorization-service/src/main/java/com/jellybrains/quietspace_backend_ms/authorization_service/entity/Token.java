package com.jellybrains.quietspace_backend_ms.authorization_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @NotBlank
    private String jwtToken;

    @NotNull
    @NotBlank
    private String email;

}
