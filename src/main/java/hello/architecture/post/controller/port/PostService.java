package hello.architecture.post.controller.port;

import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostCreate;
import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.domain.PostUpdate;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    Post write(PostCreate request, LocalDateTime createAt);

    Post update(Long id, PostUpdate request, LocalDateTime modifiedAt);

    List<Post> findAll();

    Post findById(Long id);

    List<Post> findByWriterIdAndStatus(Long writerId, PostStatus status);
}
