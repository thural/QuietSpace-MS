package com.jellybrains.quietspace_backend_ms.feedservice.controller;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.request.PostRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.PostLikeResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.PostResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.service.PostService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PostController {

    public static final String POST_PATH = "/api/v1/posts";
    public static final String POST_PATH_ID = POST_PATH + "/{postId}";

    private final PostService postService;

    @RequestMapping(value = POST_PATH, method = RequestMethod.GET)
    Page<PostResponse> getAllPosts(@RequestParam(name = "page-number", required = false) Integer pageNumber,
                                   @RequestParam(name = "page-size", required = false) Integer pageSize) {
        return postService.getAllPosts(pageNumber, pageSize);
    }

    @RequestMapping(value = POST_PATH, method = RequestMethod.POST)
    ResponseEntity<?> createPost(@RequestBody @Validated PostRequest post) {
        PostResponse savedPost = postService.addPost(post);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", POST_PATH + "/" + savedPost.getId());
        System.out.println("post resource location" + headers.get("Location"));
        return new ResponseEntity<>(savedPost, headers, HttpStatus.OK);
    }

    @RequestMapping(value = POST_PATH_ID, method = RequestMethod.GET)
    PostResponse getPostById(@PathVariable("postId") UUID id) {
        Optional<PostResponse> optionalPost = postService.getPostById(id);
        return optionalPost.orElse(null);
    }

    @RequestMapping(value = POST_PATH_ID, method = RequestMethod.PUT)
    ResponseEntity<?> putPost(@PathVariable("postId") UUID id,
                              @RequestBody @Validated PostRequest post) {
        postService.updatePost(id, post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = POST_PATH_ID, method = RequestMethod.DELETE)
    ResponseEntity<?> deletePost(@PathVariable("postId") UUID id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = POST_PATH_ID, method = RequestMethod.PATCH)
    ResponseEntity<?> patchPost(@PathVariable("postId") UUID id,
                                @RequestBody PostRequest post) {
        postService.patchPost(id, post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = POST_PATH_ID + "/likes", method = RequestMethod.GET)
    List<PostLikeResponse> getAllLikesByPostId(@PathVariable("postId") UUID postId) {
        return postService.getPostLikesByPostId(postId);
    }

    @RequestMapping(value = POST_PATH_ID + "/toggle-like", method = RequestMethod.POST)
    ResponseEntity<?> togglePostLike(@PathVariable UUID postId) {
        postService.togglePostLike(postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}