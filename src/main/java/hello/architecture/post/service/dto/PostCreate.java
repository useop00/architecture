package hello.architecture.post.service.dto;

import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.infrastructure.PostEntity;
import hello.architecture.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreate {

    private final Long writerId;
    private final String title;
    private final String content;
    private final PostStatus status;

    @Builder
    private PostCreate(Long writerId, String title, String content, PostStatus status) {
        this.writerId = writerId;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public PostEntity toEntity(UserEntity user) {
        return PostEntity.builder()
                .writer(user)
                .title(title)
                .content(content)
                .status(status)
                .build();
    }
}

