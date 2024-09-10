package com.jellybrains.quietspace.feed_service.kafka.consumer;

import com.jellybrains.quietspace.common_service.message.kafka.user.UserDeletionEvent;
import com.jellybrains.quietspace.feed_service.repository.CommentRepository;
import com.jellybrains.quietspace.feed_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class UserDeletionConsumer {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @KafkaListener(topics = "#{'${kafka.topics.user.deletion}'}")
    public void deleteFeedData(UserDeletionEvent event) {
        try {
            postRepository.deletePostsByUserId(event.getUserId());
            commentRepository.deleteCommentsByUserId(event.getUserId());
            log.info("feed data deleted for userId: {}", event.getUserId());
        } catch (Exception e) {
            log.info("feed data deletion failed for userId: {} cause: {}", event.getUserId(), e.getMessage());
        }
    }

}