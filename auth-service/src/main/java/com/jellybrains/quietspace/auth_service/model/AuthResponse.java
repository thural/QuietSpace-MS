package com.jellybrains.quietspace.auth_service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String userId;
    private String message;

}
