package com.jellybrains.quietspace_backend_ms.feedservice.model.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private UUID id;
    private UUID userId;
    private String username;
    private String title;
    private String text;
    private PollResponse poll;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private ReactionResponse userReaction;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}