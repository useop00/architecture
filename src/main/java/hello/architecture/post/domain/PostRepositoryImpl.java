package hello.architecture.post.domain;

import hello.architecture.common.exception.PostNotFoundException;
import hello.architecture.post.infrastructure.PostEntity;
import hello.architecture.post.service.port.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public PostEntity save(PostEntity post) {
        return postJpaRepository.save(post);
    }

    @Override
    public PostEntity findById(Long id) {
        return postJpaRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    public List<PostEntity> findAll() {
        return postJpaRepository.findAll();
    }

    @Override
    public List<PostEntity> findByWriterIdAndStatus(Long writerId, PostStatus status) {
        return postJpaRepository.findByWriterIdAndStatus(writerId, status);
    }
}
