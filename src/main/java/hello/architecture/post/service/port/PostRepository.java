package hello.architecture.post.service.port;

import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.infrastructure.PostEntity;

import java.util.List;

public interface PostRepository {

    PostEntity save(PostEntity post);

    PostEntity findById(Long id);

    List<PostEntity> findAll();

    List<PostEntity> findByWriterIdAndStatus(Long writerId, PostStatus status);
}
