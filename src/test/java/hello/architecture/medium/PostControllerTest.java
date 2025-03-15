package hello.architecture.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostCreate;
import hello.architecture.post.domain.PostUpdate;
import hello.architecture.post.service.port.PostRepository;
import hello.architecture.user.domain.User;
import hello.architecture.user.service.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hello.architecture.post.domain.PostStatus.PRIVATE;
import static hello.architecture.post.domain.PostStatus.PUBLIC;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void write() throws Exception {
        // given
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(user);
        PostCreate postCreate = PostCreate.builder()
                .writerId(savedUser.getId())
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();

        String json = objectMapper.writeValueAsString(postCreate);
        // when & then
        mockMvc.perform(post("/posts")
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.status").value("PUBLIC"));
    }

    @Test
    void findAll() throws Exception {
        // given
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(user);
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
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // when
        List<Post> posts = List.of(post1, post2, post3);

        // then
        mockMvc.perform(get("/posts")
                        .content(objectMapper.writeValueAsString(posts))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        // given
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(user);
        Post post = Post.builder()
                .writer(savedUser)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        Post savedPost = postRepository.save(post);

        // when & then
        mockMvc.perform(get("/posts/{id}", savedPost.getId())
                        .content(objectMapper.writeValueAsString(savedPost))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.status").value("PUBLIC"));
    }


    @Test
    void findByWriterIdAndStatus() throws Exception {
        // given
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(user);
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

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);


        // when & then
        mockMvc.perform(get("/posts/{id}/{status}", savedUser.getId(), "PRIVATE")
                        .content(objectMapper.writeValueAsString(savedUser))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목123"))
                .andExpect(jsonPath("$[0].content").value("내용123"));
    }

    @Test
    void update() throws Exception {
        // given
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(user);
        Post post = Post.builder()
                .writer(savedUser)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        Post savedPost = postRepository.save(post);

        // when
        PostUpdate postUpdate = PostUpdate.builder()
                .title("수정")
                .content("수정")
                .status(PRIVATE)
                .build();

        // then
        mockMvc.perform(put("/posts/{id}", savedPost.getId())
                        .content(objectMapper.writeValueAsString(postUpdate))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정"))
                .andExpect(jsonPath("$.content").value("수정"))
                .andExpect(jsonPath("$.status").value("PRIVATE"));
    }

}