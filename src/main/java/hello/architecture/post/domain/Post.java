package hello.architecture.post.domain;

import hello.architecture.post.service.dto.PostCreate;
import hello.architecture.post.service.dto.PostUpdate;
import hello.architecture.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Post {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createAt;
    private final LocalDateTime modifiedAt;
    private final User writer;
    private final PostStatus status;

    @Builder
    private Post(Long id, String title, String content, LocalDateTime createAt, LocalDateTime modifiedAt, User writer, PostStatus status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
        this.status = status;
    }

    public static Post from(PostCreate postCreate, User writer, LocalDateTime createAt) {
        return Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .createAt(createAt)
                .writer(writer)
                .status(postCreate.getStatus())
                .build();
    }

    public Post update(PostUpdate postUpdate, LocalDateTime modifiedAt) {
        return Post.builder()
                .id(this.id)
                .title(postUpdate.getTitle())
                .content(postUpdate.getContent())
                .createAt(createAt)
                .modifiedAt(modifiedAt)
                .writer(this.writer)
                .status(postUpdate.getStatus() == null ? this.status : postUpdate.getStatus())
                .build();
    }
}
