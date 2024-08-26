package com.jellybrains.quietspace_backend_ms.userservice.repository;

import com.jellybrains.quietspace_backend_ms.userservice.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, String> {

    Page<Profile> findAllByUsernameIsLikeIgnoreCase(String userName, Pageable pageable);

    Optional<Profile> findByEmail(String email);

    @Query("SELECT p FROM Profile p WHERE p.username LIKE %:query% OR p.email LIKE %:query%")
    Page<Profile> findAllByQuery(String query, PageRequest pageRequest);

    Optional<Profile> findByUsername(String username);

    Optional<Profile> findByUserId(String userId);

//    @Query("SELECT p FROM Profile p WHERE p.userId MEMBER OF :userIds")
//    Page<Profile> findAllByUserIdContaining(@Param("userIds") List<String> userIds, PageRequest pageRequest);

}
