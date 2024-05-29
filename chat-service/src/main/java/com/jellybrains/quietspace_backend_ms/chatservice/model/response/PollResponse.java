package com.jellybrains.quietspace_backend_ms.chatservice.model.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollResponse {

    private UUID id;
    private String votedOption;
    private Integer voteCount;
    private List<OptionResponse> options;
    private OffsetDateTime dueDate;

}