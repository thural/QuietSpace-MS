package com.jellybrains.quietspace_backend_ms.chatservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Post {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Integer version;

    private String title;

    @NotNull
    @NotBlank
    private String text;

    @NotNull
    @ManyToOne
    private User user;

    @JsonIgnore
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Poll poll;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @NotNull
    @Column(updatable = false)
    private OffsetDateTime createDate;

    @NotNull
    private OffsetDateTime updateDate;

    @PrePersist
    private void onCreate() {
        createDate = OffsetDateTime.now();
        updateDate = OffsetDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        updateDate = OffsetDateTime.now();
    }

}
