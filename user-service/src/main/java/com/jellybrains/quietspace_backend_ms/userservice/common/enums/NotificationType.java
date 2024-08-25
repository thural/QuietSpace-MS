package com.jellybrains.quietspace_backend_ms.userservice.common.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    FOLLOW_REQUEST,
    POST_REACTION,
    COMMENT,
    COMMENT_REACTION,
    COMMENT_REPLY,
    REPOST
}
