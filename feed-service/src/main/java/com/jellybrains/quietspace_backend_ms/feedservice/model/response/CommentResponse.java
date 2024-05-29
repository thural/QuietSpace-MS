package com.jellybrains.quietspace_backend_ms.feedservice.model.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private UUID id;
    private UUID userId;
    private UUID postId;
    private String text;
    private UUID parentId;
    private String username;
    private Integer likeCount;
    private Integer replyCount;
    private ReactionResponse userReaction;
    private OffsetDateTime createDate = OffsetDateTime.now();
    private OffsetDateTime updateDate = OffsetDateTime.now();

}
