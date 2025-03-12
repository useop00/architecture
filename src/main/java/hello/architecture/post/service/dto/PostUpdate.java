package hello.architecture.post.service.dto;

import hello.architecture.post.domain.PostStatus;
import lombok.Getter;

@Getter
public class PostUpdate {

    private final String title;
    private final String content;
    private final PostStatus status;

    public PostUpdate(String title, String content, PostStatus status) {
        this.title = title;
        this.content = content;
        this.status = status;
    }


}
