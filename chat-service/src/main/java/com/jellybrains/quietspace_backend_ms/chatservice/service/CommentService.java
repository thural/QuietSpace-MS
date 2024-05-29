package com.jellybrains.quietspace_backend_ms.chatservice.service;

import dev.thural.quietspace.model.request.CommentRequest;
import dev.thural.quietspace.model.response.CommentResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface CommentService {

    Page<CommentResponse> getCommentsByPost(UUID postId, Integer pageNumber, Integer pageSize);

    CommentResponse createComment(CommentRequest comment);

    Optional<CommentResponse> getCommentById(UUID id);

    CommentResponse updateComment(UUID commentId, CommentRequest comment);

    void deleteComment(UUID id);

    Page<CommentResponse> getRepliesByParentId(UUID commentId, Integer pageNumber, Integer pageSize);

    CommentResponse patchComment(UUID id, CommentRequest comment);

    Page<CommentResponse> getCommentsByUser(UUID userId, Integer pageNumber, Integer pageSize);
}
