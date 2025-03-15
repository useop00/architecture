package hello.architecture.post.infrastructure;

import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.service.port.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(PostEntity.from(post)).toModel();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id).map(PostEntity::toModel);
    }

    @Override
    public List<Post> findAll() {
        return postJpaRepository.findAll().stream()
                .map(PostEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Post> findByWriterIdAndStatus(Long writerId, PostStatus status) {
        return postJpaRepository.findByWriterIdAndStatus(writerId, status).stream()
                .map(PostEntity::toModel).collect(Collectors.toList());
    }

}
