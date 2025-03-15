package hello.architecture.post.infrastructure;

import hello.architecture.post.domain.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findByWriterIdAndStatus(Long writerId, PostStatus status);

}
