package com.jellybrains.quietspace_backend_ms.userservice.controller;

import com.jellybrains.quietspace_backend_ms.userservice.model.response.FollowResponse;
import com.jellybrains.quietspace_backend_ms.userservice.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follows")
public class FollowController {

    public static final String FOLLOW_PATH_ID = "/{userId}";
    public static final String FOLLOW_USER_TOGGLE = FOLLOW_PATH_ID + "/toggle-follow";

    private final FollowService followService;


    @PostMapping(FOLLOW_USER_TOGGLE)
    ResponseEntity<?> toggleFollow(@PathVariable UUID userId) {
        followService.toggleFollow(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/followings")
    Page<FollowResponse> listFollowings(@RequestParam(name = "page-number", required = false) Integer pageNumber,
                                        @RequestParam(name = "page-size", required = false) Integer pageSize) {
        return followService.listFollowings(pageNumber, pageSize);
    }

    @GetMapping("/followers")
    Page<FollowResponse> listFollowers(@RequestParam(name = "page-number", required = false) Integer pageNumber,
                                       @RequestParam(name = "page-size", required = false) Integer pageSize) {
        return followService.listFollowers(pageNumber, pageSize);
    }

}