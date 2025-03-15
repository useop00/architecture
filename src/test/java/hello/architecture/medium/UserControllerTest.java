package hello.architecture.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.architecture.user.domain.User;
import hello.architecture.user.domain.Login;
import hello.architecture.user.domain.UserCreate;
import hello.architecture.user.domain.UserUpdate;
import hello.architecture.user.service.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void create() throws Exception {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();

        // when & then
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userCreate))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("seop@naver.com"))
                .andExpect(jsonPath("$.nickname").value("seop"));
    }

    @Test
    void login() throws Exception {
        // given
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userRepository.save(user);

        Login login = Login.of(user.getEmail(), "1234");

        // when & then
        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(login))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("seop@naver.com"))
                .andExpect(jsonPath("$.nickname").value("seop"));
    }

    @Test
    void updateMyInfo() throws Exception {
        // given
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userRepository.save(user);

        // when
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("woo")
                .build();

        // then
        mockMvc.perform(put("/me")
                        .header("EMAIL", user.getEmail())
                        .content(objectMapper.writeValueAsString(userUpdate))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("woo"));
    }


}