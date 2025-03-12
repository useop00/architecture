package hello.architecture.post.controller.response;

import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.infrastructure.PostEntity;
import hello.architecture.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createAt;
    private final java.time.LocalDateTime modifiedAt;
    private final UserEntity writer;
    private final PostStatus status;

    @Builder
    private PostResponse(Long id, String title, String content, LocalDateTime createAt, LocalDateTime modifiedAt, UserEntity writer, PostStatus status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
        this.status = status;
    }

    public static PostResponse of(PostEntity post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createAt(post.getCreateAt())
                .modifiedAt(post.getModifiedAt())
                .writer(post.getWriter())
                .status(post.getStatus())
                .build();
    }
}
