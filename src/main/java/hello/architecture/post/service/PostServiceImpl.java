package hello.architecture.post.service;

import com.google.common.annotations.VisibleForTesting;
import hello.architecture.common.exception.PostNotFoundException;
import hello.architecture.common.exception.UserNotFoundException;
import hello.architecture.post.controller.port.PostService;
import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.domain.PostCreate;
import hello.architecture.post.domain.PostUpdate;
import hello.architecture.post.service.port.PostRepository;
import hello.architecture.user.domain.User;
import hello.architecture.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @VisibleForTesting
    public static PostServiceImpl forTest(PostRepository postRepository,
                                          UserRepository userRepository) {
        return new PostServiceImpl(postRepository, userRepository);
    }

    @Transactional
    public Post write(PostCreate request, LocalDateTime createAt) {
        User writer = userRepository.findById(request.getWriterId())
                .orElseThrow(UserNotFoundException::new);
        Post post = Post.from(request, writer, createAt);

        return postRepository.save(post);
    }

    @Transactional
    public Post update(Long id, PostUpdate request, LocalDateTime modifiedAt) {
        Post post = findById(id);
        post = post.update(request, modifiedAt);

        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
    }

    public List<Post> findByWriterIdAndStatus(Long writerId, PostStatus status) {
        return postRepository.findByWriterIdAndStatus(writerId, status);
    }
}
