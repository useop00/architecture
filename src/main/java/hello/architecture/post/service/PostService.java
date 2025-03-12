package hello.architecture.post.service;

import hello.architecture.post.controller.response.PostResponse;
import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.infrastructure.PostEntity;
import hello.architecture.post.service.dto.PostCreate;
import hello.architecture.post.service.dto.PostUpdate;
import hello.architecture.post.service.port.PostRepository;
import hello.architecture.user.infrastructure.UserEntity;
import hello.architecture.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse write(PostCreate request) {
        UserEntity writer = userRepository.findById(request.getWriterId());
        PostEntity post = request.toEntity(writer);

        return PostResponse.of(postRepository.save(post));
    }

    @Transactional
    public PostResponse update(Long id, PostUpdate request) {
        PostEntity post = postRepository.findById(id);

        post.update(request.getTitle(), request.getContent(), request.getStatus());
        return PostResponse.of(postRepository.save(post));
    }

    public List<PostResponse> findAll() {
        return postRepository.findAll().stream()
                .map(PostResponse::of)
                .toList();
    }

    public PostResponse findById(Long id) {
        PostEntity post = postRepository.findById(id);
        return PostResponse.of(post);
    }

    public List<PostResponse> findByWriterIdAndStatus(Long writerId, PostStatus status) {
        return postRepository.findByWriterIdAndStatus(writerId, status).stream()
                .map(PostResponse::of)
                .toList();
    }

}
