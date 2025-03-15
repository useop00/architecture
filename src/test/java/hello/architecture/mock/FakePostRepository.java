package hello.architecture.mock;

import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.service.port.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakePostRepository implements PostRepository {

    private final AtomicLong autoIncrementId = new AtomicLong(1);
    private final List<Post> data = new ArrayList<>();

    @Override
    public Post save(Post post) {
        if (post.getId() == null) {
            Post newPost = Post.builder()
                    .id(autoIncrementId.getAndIncrement())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .writer(post.getWriter())
                    .status(post.getStatus())
                    .createAt(post.getCreateAt())
                    .modifiedAt(post.getModifiedAt())
                    .build();
            data.add(newPost);
            return newPost;
        } else {
            data.removeIf(p -> p.getId().equals(post.getId()));
            data.add(post);
            return post;
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        return data.stream()
                .filter(post -> post.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Post> findAll() {
        return data.stream().toList();
    }

    @Override
    public List<Post> findByWriterIdAndStatus(Long writerId, PostStatus status) {
        return data.stream()
                .filter(post -> post.getWriter().getId().equals(writerId) && post.getStatus() == status)
                .toList();
    }
}
