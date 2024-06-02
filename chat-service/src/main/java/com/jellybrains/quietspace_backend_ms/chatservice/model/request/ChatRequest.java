package com.jellybrains.quietspace_backend_ms.chatservice.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRequest {

    @NotNull(message = "at least two members required to create the chat")
    private List<UUID> userIds;

}
