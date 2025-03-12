package hello.architecture.post.service;

import hello.architecture.common.exception.PostNotFoundException;
import hello.architecture.post.controller.response.PostResponse;
import hello.architecture.post.domain.PostJpaRepository;
import hello.architecture.post.infrastructure.PostEntity;
import hello.architecture.post.service.dto.PostCreate;
import hello.architecture.post.service.dto.PostUpdate;
import hello.architecture.user.domain.UserJpaRepository;
import hello.architecture.user.infrastructure.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hello.architecture.post.domain.PostStatus.PRIVATE;
import static hello.architecture.post.domain.PostStatus.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Test
    void write() throws Exception {
        // given
        UserEntity writer = UserEntity.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userJpaRepository.save(writer);

        PostCreate postCreate = PostCreate.builder()
                .writerId(writer.getId())
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();

        // when
        PostResponse result = postService.write(postCreate);

        // then
        assertThat(result.getContent()).isEqualTo("내용");
        assertThat(result.getTitle()).isEqualTo("제목");
        assertThat(result.getStatus()).isEqualTo(PUBLIC);
        assertThat(result.getWriter().getEmail()).isEqualTo("seop@naver.com");
    }

    @Test
    void findById() throws Exception {
        // given
        PostEntity post1 = PostEntity.builder()
                .title("제목1")
                .content("내용1")
                .status(PUBLIC)
                .build();
        PostEntity post2 = PostEntity.builder()
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        PostEntity post3 = PostEntity.builder()
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        postJpaRepository.saveAll(List.of(post1, post2, post3));

        // when
        PostResponse result = postService.findById(1L);

        // then
        assertThat(result.getTitle()).isEqualTo("제목1");
        assertThat(result.getContent()).isEqualTo("내용1");
    }

    @Test
    void notFoundById() throws Exception {
        // given
        long postId = 1L;

        //expect
        assertThatThrownBy(() ->
                postService.findById(postId)
        ).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void findAll() throws Exception {
        // given
        PostEntity post1 = PostEntity.builder()
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        PostEntity post2 = PostEntity.builder()
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        PostEntity post3 = PostEntity.builder()
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        postJpaRepository.saveAll(List.of(post1, post2, post3));

        // when
        List<PostResponse> result = postService.findAll();

        // then
        assertThat(result).hasSize(3);
    }

    @Test
    void findByWriterIdAndStatus() throws Exception {
        // given
        UserEntity writer = UserEntity.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userJpaRepository.save(writer);
        PostEntity post1 = PostEntity.builder()
                .writer(writer)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        PostEntity post2 = PostEntity.builder()
                .writer(writer)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        PostEntity post3 = PostEntity.builder()
                .writer(writer)
                .title("제목ㅋ.ㅋ.ㅋ")
                .content("내용")
                .status(PRIVATE)
                .build();
        postJpaRepository.saveAll(List.of(post1, post2, post3));

        // when
        List<PostResponse> result = postService.findByWriterIdAndStatus(post1.getWriter().getId(), PRIVATE);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("제목ㅋ.ㅋ.ㅋ");
    }


    @Test
    void update() throws Exception {
        // given
        UserEntity writer = UserEntity.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userJpaRepository.save(writer);

        PostEntity post = PostEntity.builder()
                .writer(writer)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        postJpaRepository.save(post);

        PostUpdate postUpdate = PostUpdate.builder()
                .title("라멘")
                .content("먹고싶다")
                .build();

        // when
        PostResponse result = postService.update(post.getId(), postUpdate);

        // then
        assertThat(result.getTitle()).isEqualTo("라멘");
        assertThat(result.getContent()).isEqualTo("먹고싶다");
        assertThat(result.getStatus()).isEqualTo(PUBLIC);
        assertThat(result.getWriter().getEmail()).isEqualTo("seop@naver.com");
    }


}