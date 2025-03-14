package hello.architecture.post.service.dto;

import hello.architecture.post.domain.PostStatus;
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
}

