package hello.architecture.medium;

import hello.architecture.post.infrastructure.PostEntity;
import hello.architecture.post.infrastructure.PostJpaRepository;
import hello.architecture.user.infrastructure.UserJpaRepository;
import hello.architecture.user.infrastructure.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static hello.architecture.post.domain.PostStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostJpaRepositoryTest {

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
        postJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
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
                .title("제목")
                .content("내용")
                .status(PRIVATE)
                .build();
        postJpaRepository.saveAll(List.of(post1, post2, post3));
        // when
        List<PostEntity> result = postJpaRepository.findByWriterIdAndStatus(writer.getId(), PUBLIC);

        // then
        assertThat(result).hasSize(2);
    }

}