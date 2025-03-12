package hello.architecture.post.service;

import hello.architecture.common.exception.PostNotFoundException;
import hello.architecture.common.exception.UserNotFoundException;
import hello.architecture.post.controller.response.PostResponse;
import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostJpaRepository;
import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.infrastructure.PostEntity;
import hello.architecture.post.service.dto.PostCreate;
import hello.architecture.post.service.dto.PostUpdate;
import hello.architecture.post.service.port.PostRepository;
import hello.architecture.user.domain.UserJpaRepository;
import hello.architecture.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public PostResponse write(PostCreate request) {
        UserEntity writer = userJpaRepository.findById(request.getWriterId())
                .orElseThrow(UserNotFoundException::new);
        PostEntity post = request.toEntity(writer);

        return PostResponse.of(postJpaRepository.save(post));
    }

    @Transactional
    public PostResponse update(Long id, PostUpdate request) {
        PostEntity post = postJpaRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        post.update(request.getTitle(), request.getContent(), request.getStatus());
        return PostResponse.of(postJpaRepository.save(post));
    }

    public List<PostResponse> findAll() {
        return postJpaRepository.findAll().stream()
                .map(PostResponse::of)
                .toList();
    }

    public PostResponse findById(Long id) {
        PostEntity post = postJpaRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        return PostResponse.of(post);
    }

    public List<PostResponse> findByWriterIdAndStatus(Long writerId, PostStatus status) {
        return postJpaRepository.findByWriterIdAndStatus(writerId, status).stream()
                .map(PostResponse::of)
                .toList();
    }

}
