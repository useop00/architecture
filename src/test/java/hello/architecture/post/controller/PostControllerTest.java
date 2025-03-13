package hello.architecture.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.architecture.post.controller.response.PostResponse;
import hello.architecture.post.infrastructure.PostEntity;
import hello.architecture.post.service.PostService;
import hello.architecture.post.service.dto.PostCreate;
import hello.architecture.post.service.dto.PostUpdate;
import hello.architecture.post.service.port.PostRepository;
import hello.architecture.user.infrastructure.UserEntity;
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

    @Autowired
    private PostService postService;

    @Test
    void write() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userRepository.save(user);
        PostCreate postCreate = PostCreate.builder()
                .writerId(user.getId())
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
        PostEntity post1 = PostEntity.builder()
                .title("제목1")
                .content("내용1")
                .status(PUBLIC)
                .build();
        PostEntity post2 = PostEntity.builder()
                .title("제목2")
                .content("내용2")
                .status(PUBLIC)
                .build();
        PostEntity post3 = PostEntity.builder()
                .title("제목3")
                .content("내용3")
                .status(PUBLIC)
                .build();
        postRepository.saveAll(List.of(post1, post2, post3));

        // when
        List<PostResponse> result = postService.findAll();

        // then
        mockMvc.perform(get("/posts")
                        .content(objectMapper.writeValueAsString(result))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        // given
        PostEntity post = PostEntity.builder()
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        postRepository.save(post);

        // when & then
        mockMvc.perform(get("/posts/{id}", post.getId())
                        .content(objectMapper.writeValueAsString(post))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.status").value("PUBLIC"));
    }


    @Test
    void findByWriterIdAndStatus() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userRepository.save(user);
        PostEntity post = PostEntity.builder()
                .writer(user)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        postRepository.save(post);

        // when
        List<PostResponse> result = postService.findByWriterIdAndStatus(user.getId(), PUBLIC);

        // then
        mockMvc.perform(get("/posts/{id}/{status}", post.getId(), post.getStatus())
                        .content(objectMapper.writeValueAsString(result))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목"))
                .andExpect(jsonPath("$[0].content").value("내용"));
    }

    @Test
    void update() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userRepository.save(user);
        PostEntity post = PostEntity.builder()
                .writer(user)
                .title("제목")
                .content("내용")
                .status(PUBLIC)
                .build();
        postRepository.save(post);

        // when
        PostUpdate postUpdate = PostUpdate.builder()
                .title("수정")
                .content("수정")
                .status(PRIVATE)
                .build();

        // then
        mockMvc.perform(put("/posts/{id}", post.getId())
                        .content(objectMapper.writeValueAsString(postUpdate))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정"))
                .andExpect(jsonPath("$.content").value("수정"))
                .andExpect(jsonPath("$.status").value("PRIVATE"));
    }

}