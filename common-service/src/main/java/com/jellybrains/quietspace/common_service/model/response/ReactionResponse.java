package com.jellybrains.quietspace.common_service.model.response;

import com.jellybrains.quietspace.common_service.enums.ReactionType;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionResponse {

    private String id;
    private String userId;
    private String contentId;
    private String username;
    private ReactionType reactionType;
    private OffsetDateTime updateDate;

}
