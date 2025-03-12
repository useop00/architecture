package hello.architecture.post.domain;

import hello.architecture.post.infrastructure.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findByWriterIdAndStatus(Long writerId, PostStatus status);
}
