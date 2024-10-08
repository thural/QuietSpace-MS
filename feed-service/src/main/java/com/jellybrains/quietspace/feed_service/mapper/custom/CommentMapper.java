package com.jellybrains.quietspace.feed_service.mapper.custom;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.model.request.CommentRequest;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.common_service.service.shared.ReactionService;
import com.jellybrains.quietspace.common_service.service.shared.UserService;
import com.jellybrains.quietspace.feed_service.entity.Comment;
import com.jellybrains.quietspace.feed_service.entity.Post;
import com.jellybrains.quietspace.feed_service.repository.CommentRepository;
import com.jellybrains.quietspace.feed_service.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ReactionService reactionService;


    public Comment toEntity(CommentRequest comment) {
        return Comment.builder()
                .parentId(comment.getParentId())
                .text(comment.getText())
                .userId(comment.getUserId())
                .post(getPostById(comment.getPostId())).build();
    }

    public CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .parentId(comment.getParentId())
                .postId(comment.getPost().getId())
                .userId(comment.getUserId())
                .username(userService.getUsernameById(comment.getUserId()))
                .text(comment.getText())
                .userReaction(reactionService.getUserReactionByContentId(comment.getId(), ContentType.COMMENT))
                .createDate(comment.getCreateDate())
                .updateDate(comment.getUpdateDate())
                .likeCount(reactionService.getLikeCount(comment.getId()))
                .replyCount(getReplyCount(comment.getId(), comment.getPost())).build();
    }

    private Post getPostById(String postId) {
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.error(EntityNotFoundException::new)).block();
    }

    private Integer getReplyCount(String parentId, Post post) {
        return commentRepository.countByParentIdAndPost(parentId, post)
                .switchIfEmpty(Mono.error(EntityNotFoundException::new)).block();
    }

}
