package hello.architecture.post.domain;

import hello.architecture.user.domain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static hello.architecture.post.domain.PostStatus.PRIVATE;
import static hello.architecture.post.domain.PostStatus.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void write() throws Exception {
        // given
        LocalDateTime createAt = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .writerId(user.getId())
                .build();

        // when
        Post post = Post.from(postCreate, user, createAt);

        // then
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getStatus()).isEqualTo(PUBLIC);
        assertThat(post.getWriter().getNickname()).isEqualTo("seop");
    }

    @Test
    void update() throws Exception {
        // given
        LocalDateTime createAt = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        Post post = Post.builder()
                .id(1L)
                .writer(user)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .createAt(createAt)
                .build();

        PostUpdate postUpdate = PostUpdate.builder()
                .title("라멘")
                .content("먹고싶다")
                .status(PRIVATE)
                .build();

        // when
        post = post.update(postUpdate, LocalDateTime.now());

        // then
        assertThat(post.getTitle()).isEqualTo("라멘");
        assertThat(post.getContent()).isEqualTo("먹고싶다");
        assertThat(post.getStatus()).isEqualTo(PRIVATE);
        assertThat(post.getWriter().getNickname()).isEqualTo("seop");
    }

}