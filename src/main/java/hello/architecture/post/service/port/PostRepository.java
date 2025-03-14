package hello.architecture.post.service.port;

import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostStatus;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAll();

    List<Post> findByWriterIdAndStatus(Long writerId, PostStatus status);

}
