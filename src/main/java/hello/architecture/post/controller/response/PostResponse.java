package hello.architecture.post.controller.response;

import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostStatus;
import hello.architecture.user.controller.response.UserResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createAt;
    private final java.time.LocalDateTime modifiedAt;
    private final UserResponse writer;
    private final PostStatus status;

    @Builder
    private PostResponse(Long id, String title, String content, LocalDateTime createAt, LocalDateTime modifiedAt, UserResponse writer, PostStatus status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
        this.status = status;
    }

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createAt(post.getCreateAt())
                .modifiedAt(post.getModifiedAt())
                .writer(UserResponse.of(post.getWriter()))
                .status(post.getStatus())
                .build();
    }

    public static List<PostResponse> of(List<Post> posts) {
        return posts.stream()
                .map(PostResponse::of)
                .toList();
    }
}
