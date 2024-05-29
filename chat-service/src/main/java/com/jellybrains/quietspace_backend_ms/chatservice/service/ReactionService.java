package com.jellybrains.quietspace_backend_ms.chatservice.service;

import dev.thural.quietspace.model.request.ReactionRequest;
import dev.thural.quietspace.model.response.ReactionResponse;
import dev.thural.quietspace.utils.enums.ContentType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReactionService {

    void handleReaction(ReactionRequest reaction);

    Optional<ReactionResponse> getUserReactionByContentId(UUID contentId);

    List<ReactionResponse> getLikesByContentId(UUID contentId);

    List<ReactionResponse> getDislikesByContentId(UUID contentId);

    Integer getLikeCountByContentId(UUID contentId);

    Integer getDislikeCountByContentId(UUID contentId);

    List<ReactionResponse> getReactionsByContentId(UUID contentId, ContentType type);

    List<ReactionResponse> getReactionsByUserId(UUID userId, ContentType contentType);

}
