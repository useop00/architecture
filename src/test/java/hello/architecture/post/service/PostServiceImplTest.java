package hello.architecture.post.service;

import hello.architecture.common.exception.PostNotFoundException;
import hello.architecture.mock.FakePostRepository;
import hello.architecture.mock.FakeUserRepository;
import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostCreate;
import hello.architecture.post.domain.PostUpdate;
import hello.architecture.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static hello.architecture.post.domain.PostStatus.PRIVATE;
import static hello.architecture.post.domain.PostStatus.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostServiceImplTest {

    private PostServiceImpl postServiceImpl;

    @BeforeEach
    void initialize() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.postServiceImpl = PostServiceImpl.forTest(
                fakePostRepository,
                fakeUserRepository
        );
        User user = fakeUserRepository.save(User.builder()
                .id(1L)
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build());
        fakePostRepository.save(Post.builder()
                .id(1L)
                .writer(user)
                .title("제목1")
                .content("내용1")
                .status(PUBLIC)
                .build());
        fakePostRepository.save(Post.builder()
                .id(2L)
                .writer(user)
                .title("제목2")
                .content("내용2")
                .status(PUBLIC)
                .build());
        fakePostRepository.save(Post.builder()
                .id(3L)
                .writer(user)
                .title("제목3")
                .content("내용3")
                .status(PRIVATE)
                .build());
    }

    @Test
    void write() throws Exception {
        // given
        LocalDateTime createAt = LocalDateTime.now();
        PostCreate postCreate = PostCreate.builder()
                .writerId(1L)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();

        // when
        Post result = postServiceImpl.write(postCreate, createAt);

        // then
        assertThat(result.getContent()).isEqualTo("내용");
        assertThat(result.getTitle()).isEqualTo("제목");
        assertThat(result.getWriter().getEmail()).isEqualTo("seop@naver.com");
        assertThat(result.getWriter().getNickname()).isEqualTo("seop");
        assertThat(result.getStatus()).isEqualTo(PUBLIC);
        assertThat(result.getCreateAt()).isEqualTo(createAt);
    }

    @Test
    void findById() throws Exception {
        // given
        Long postId = 1L;

        // when
        Post result = postServiceImpl.findById(postId);

        // then
        assertThat(result.getTitle()).isEqualTo("제목1");
        assertThat(result.getContent()).isEqualTo("내용1");
    }

    @Test
    void notFoundById() throws Exception {
        // given
        long postId = 5L;

        //expect
        assertThatThrownBy(() ->
                postServiceImpl.findById(postId)
        ).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void findAll() throws Exception {
        // given
        // when
        List<Post> result = postServiceImpl.findAll();

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getWriter().getEmail()).isEqualTo("seop@naver.com");
    }

    @Test
    void findByWriterIdAndStatus() throws Exception {
        // given
        // when
        List<Post> result = postServiceImpl.findByWriterIdAndStatus(1L, PRIVATE);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("제목3");
        assertThat(result.get(0).getContent()).isEqualTo("내용3");
        assertThat(result.get(0).getWriter().getNickname()).isEqualTo("seop");
    }


    @Test
    void update() throws Exception {
        // given
        LocalDateTime modifyAt = LocalDateTime.now();
        PostUpdate postUpdate = PostUpdate.builder()
                .title("라멘")
                .content("먹고싶다")
                .status(PRIVATE)
                .build();

        // when
        Post result = postServiceImpl.update(1L, postUpdate, modifyAt);

        // then
        assertThat(result.getTitle()).isEqualTo("라멘");
        assertThat(result.getContent()).isEqualTo("먹고싶다");
        assertThat(result.getStatus()).isEqualTo(PRIVATE);
        assertThat(result.getWriter().getEmail()).isEqualTo("seop@naver.com");
        assertThat(result.getModifiedAt()).isEqualTo(modifyAt);
    }


}