package hello.architecture.post.service;

import hello.architecture.common.exception.PostNotFoundException;
import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostCreate;
import hello.architecture.post.domain.PostUpdate;
import hello.architecture.post.service.port.PostRepository;
import hello.architecture.user.domain.User;
import hello.architecture.user.service.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static hello.architecture.post.domain.PostStatus.PRIVATE;
import static hello.architecture.post.domain.PostStatus.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PostServiceImplTest {

    @Autowired
    private PostServiceImpl postServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void write() throws Exception {
        // given
        LocalDateTime createAt = LocalDateTime.now();
        User writer = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(writer);

        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .writerId(savedUser.getId())
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
        User writer = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(writer);
        Post post = Post.builder()
                .title("제목1")
                .content("내용1")
                .status(PUBLIC)
                .writer(savedUser)
                .build();
        Post savedPost = postRepository.save(post);

        // when
        Post result = postServiceImpl.findById(savedPost.getId());

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
                postServiceImpl.findById(postId)
        ).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void findAll() throws Exception {
        // given
        User writer = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(writer);
        Post post1 = Post.builder()
                .writer(savedUser)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        Post post2 = Post.builder()
                .writer(savedUser)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        Post post3 = Post.builder()
                .writer(savedUser)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // when
        List<Post> result = postServiceImpl.findAll();

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getWriter().getEmail()).isEqualTo("seop@naver.com");
    }

    @Test
    void findByWriterIdAndStatus() throws Exception {
        // given
        User writer = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(writer);
        Post post1 = Post.builder()
                .writer(savedUser)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        Post post2 = Post.builder()
                .writer(savedUser)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        Post post3 = Post.builder()
                .writer(savedUser)
                .title("제목ㅋ.ㅋ.ㅋ")
                .content("내용")
                .status(PRIVATE)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);


        // when
        List<Post> result = postServiceImpl.findByWriterIdAndStatus(savedUser.getId(), PRIVATE);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("제목ㅋ.ㅋ.ㅋ");
    }


    @Test
    void update() throws Exception {
        // given
        LocalDateTime modifyAt = LocalDateTime.now();
        User writer = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(writer);

        Post post = Post.builder()
                .writer(savedUser)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        Post savedPost = postRepository.save(post);

        PostUpdate postUpdate = PostUpdate.builder()
                .title("라멘")
                .content("먹고싶다")
                .status(PRIVATE)
                .build();

        // when
        Post result = postServiceImpl.update(savedPost.getId(), postUpdate, modifyAt);

        // then
        assertThat(result.getTitle()).isEqualTo("라멘");
        assertThat(result.getContent()).isEqualTo("먹고싶다");
        assertThat(result.getStatus()).isEqualTo(PRIVATE);
        assertThat(result.getWriter().getEmail()).isEqualTo("seop@naver.com");
    }


}