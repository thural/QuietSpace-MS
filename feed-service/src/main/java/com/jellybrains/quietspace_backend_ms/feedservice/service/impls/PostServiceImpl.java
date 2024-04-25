package com.jellybrains.quietspace_backend_ms.feedservice.service.impls;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.request.PostRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.PostLikeResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.PostResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.mapper.PostMapper;
import com.jellybrains.quietspace_backend_ms.feedservice.model.Post;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.PostLikeRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.PostRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.feedservice.utils.PagingProvider.buildCustomPageRequest;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
//    private final UserRepository userRepository;

    public final String AUTHOR_MISMATCH_MESSAGE = "post author mismatch with current user";

    @Override
    public Page<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildCustomPageRequest(pageNumber, pageSize);
        return postRepository.findAll(pageRequest).map(postMapper::postEntityToResponse);
    }

    @Override
    public PostResponse addPost(PostRequest post) {
        User loggedUser = getUserFromSecurityContext();
        Post postEntity = postMapper.postRequestToEntity(post);
        postEntity.setUser(loggedUser);
        return postMapper.postEntityToResponse(postRepository.save(postEntity));
    }

    private User getUserFromSecurityContext() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    @Override
    public Optional<PostResponse> getPostById(UUID postId) {
        Post post = findPostEntityById(postId);
        PostResponse postResponse = postMapper.postEntityToResponse(post);
        return Optional.of(postResponse);
    }

    private Post findPostEntityById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void updatePost(UUID postId, PostRequest post) {
        User loggedUser = getUserFromSecurityContext();
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost, loggedUser);
        if (postExistsByLoggedUser) {
            existingPost.setText(post.getTextContent());
            postRepository.save(existingPost);
        } else throw new AccessDeniedException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public void patchPost(UUID postId, PostRequest post) {
        User loggedUser = getUserFromSecurityContext();
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost, loggedUser);
        if (postExistsByLoggedUser) {
            if (StringUtils.hasText(post.getTextContent())) existingPost.setText(post.getTextContent());
            postRepository.save(existingPost);
        } else throw new AccessDeniedException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public void deletePost(UUID postId) {
        User loggedUser = getUserFromSecurityContext();
        Post existingPost = findPostEntityById(postId);
        boolean postExistsByLoggedUser = isPostExistsByLoggedUser(existingPost, loggedUser);
        if (postExistsByLoggedUser) postRepository.deleteById(postId);
        else throw new AccessDeniedException(AUTHOR_MISMATCH_MESSAGE);
    }

    @Override
    public Page<PostResponse> getPostsByUserId(UUID userId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildCustomPageRequest(pageNumber, pageSize);
        Page<Post> postPage;
        if (userId != null) {
            postPage = postRepository.findAllByUserId(userId, pageRequest);
        } else {
            postPage = postRepository.findAll(pageRequest);
        }
        return postPage.map(postMapper::postEntityToResponse);
    }

    private boolean isPostExistsByLoggedUser(Post existingPost, User loggedUser) {
        return existingPost.getUser().equals(loggedUser);
    }

    @Override
    public List<PostLikeResponse> getPostLikesByPostId(UUID postId) {
        return postLikeRepository.findAllByPostId(postId);
    }

    @Override
    public List<PostLikeResponse> getPostLikesByUserId(UUID userId) {
        return postLikeRepository.findAllByUserId(userId);
    }

    @Override
    public void togglePostLike(UUID postId) {
        User user = getUserFromSecurityContext();
        boolean isPostLikeExists = postLikeRepository.existsByPostIdAndUserId(postId, user.getId());
        if (isPostLikeExists) postLikeRepository.deleteById(postId);
        else {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("post not found"));
            postLikeRepository.save(PostLike.builder().post(post).user(user).build());
        }
    }

}
