package hello.architecture.post.controller;

import hello.architecture.mock.TestContainer;
import hello.architecture.post.controller.response.PostResponse;
import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostCreate;
import hello.architecture.post.domain.PostUpdate;
import hello.architecture.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static hello.architecture.post.domain.PostStatus.PRIVATE;
import static hello.architecture.post.domain.PostStatus.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;

class PostControllerTest {

    @Test
    void write() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = testContainer.userRepository.save(user);
        PostCreate postCreate = PostCreate.builder()
                .writerId(savedUser.getId())
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();

        // when
        ResponseEntity<PostResponse> response = testContainer.postController.write(postCreate);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getWriter().getNickname()).isEqualTo("seop");
        assertThat(response.getBody().getTitle()).isEqualTo("제목");
        assertThat(response.getBody().getContent()).isEqualTo("내용");
        assertThat(response.getBody().getStatus()).isEqualTo(PUBLIC);
    }

    @Test
    void findAll() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = testContainer.userRepository.save(user);
        Post post1 = Post.builder()
                .writer(savedUser)
                .title("제목1")
                .content("내용1")
                .status(PUBLIC)
                .build();
        Post post2 = Post.builder()
                .writer(savedUser)
                .title("제목2")
                .content("내용2")
                .status(PUBLIC)
                .build();
        Post post3 = Post.builder()
                .writer(savedUser)
                .title("제목3")
                .content("내용3")
                .status(PUBLIC)
                .build();
        testContainer.postRepository.save(post1);
        testContainer.postRepository.save(post2);
        testContainer.postRepository.save(post3);

        // when
        ResponseEntity<List<PostResponse>> response = testContainer.postController.findAll();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(3);
    }

    @Test
    void findById() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = testContainer.userRepository.save(user);
        Post post = Post.builder()
                .writer(savedUser)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        Post savedPost = testContainer.postRepository.save(post);

        // when
        ResponseEntity<PostResponse> response = testContainer.postController.findById(savedPost.getId());

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getWriter().getNickname()).isEqualTo("seop");
        assertThat(response.getBody().getTitle()).isEqualTo("제목");
        assertThat(response.getBody().getContent()).isEqualTo("내용");
    }


    @Test
    void findByWriterIdAndStatus() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = testContainer.userRepository.save(user);
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
                .title("제목123")
                .content("내용123")
                .status(PRIVATE)
                .build();

        testContainer.postRepository.save(post1);
        testContainer.postRepository.save(post2);
        testContainer.postRepository.save(post3);


        // when
        ResponseEntity<List<PostResponse>> response = testContainer.postController.findByWriterIdAndStatus(savedUser.getId(), PRIVATE);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get(0).getWriter().getNickname()).isEqualTo("seop");
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("제목123");
        assertThat(response.getBody().get(0).getContent()).isEqualTo("내용123");
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void update() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = testContainer.userRepository.save(user);
        Post post = Post.builder()
                .writer(savedUser)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        Post savedPost = testContainer.postRepository.save(post);
        PostUpdate postUpdate = PostUpdate.builder()
                .title("수정")
                .content("수정")
                .status(PRIVATE)
                .build();

        // when
        ResponseEntity<PostResponse> response = testContainer.postController.update(savedPost.getId(), postUpdate);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getWriter().getNickname()).isEqualTo("seop");
        assertThat(response.getBody().getTitle()).isEqualTo("수정");
        assertThat(response.getBody().getContent()).isEqualTo("수정");
        assertThat(response.getBody().getStatus()).isEqualTo(PRIVATE);
    }

}