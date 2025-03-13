package hello.architecture.post.infrastructure;

import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.service.dto.PostUpdate;
import hello.architecture.user.infrastructure.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity writer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostStatus status;

    @Builder
    private PostEntity(String title, String content, LocalDateTime createAt, LocalDateTime modifiedAt, UserEntity writer, PostStatus status) {
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
        this.status = status;
    }

    public void update(PostUpdate postUpdate) {
        this.title = postUpdate.getTitle();
        this.content = postUpdate.getContent();
        this.status = postUpdate.getStatus() != null ? postUpdate.getStatus() : this.status;
        this.modifiedAt = LocalDateTime.now();
    }
}
