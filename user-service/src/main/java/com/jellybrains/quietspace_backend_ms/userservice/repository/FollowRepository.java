package com.jellybrains.quietspace_backend_ms.userservice.repository;

import com.jellybrains.quietspace_backend_ms.userservice.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID> {

    Page<Follow> findAllByFollowerId(UUID followerId, Pageable pageable);

    Page<Follow> findAllByFollowingId(UUID followingId, Pageable pageable);

    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    void deleteByFollowerIdAndFollowingId(UUID followerId, UUID followingId);
}