package hello.architecture.user.service;

import hello.architecture.common.exception.UserNotFoundException;
import hello.architecture.user.domain.User;
import hello.architecture.user.service.dto.Login;
import hello.architecture.user.service.dto.UserCreate;
import hello.architecture.user.service.dto.UserUpdate;
import hello.architecture.user.service.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

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

        // when
        User result = userService.create(userCreate);

        // then
        assertThat(result.getEmail()).isEqualTo("seop@naver.com");
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getNickname()).isEqualTo("seop");
    }

    @Test
    void getByEmail() throws Exception {
        // given
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User savedUser = userRepository.save(user);

        // when
        User result = userService.getByEmail(savedUser.getEmail());

        // then
        assertThat(result.getNickname()).isEqualTo("seop");
    }

    @Test
    void getByEmailToFail() throws Exception {
        // given
        String email = "";

        //expect
        assertThatThrownBy(() -> userService.getByEmail(email)
        ).isInstanceOf(UserNotFoundException.class);
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

        // when
        User result = userService.login(Login.of(user.getEmail(), user.getPassword()));

        // then
        assertThat(result.getNickname()).isEqualTo("seop");
        assertThat(result.getEmail()).isEqualTo("seop@naver.com");
    }

    @Test
    void passwordDoesNotMatch() throws Exception {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User user = userService.create(userCreate);

        //expect
        assertThatThrownBy(() -> userService.login( Login.of(user.getEmail(), "3333"))
        ).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void update() throws Exception {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User user = userService.create(userCreate);
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("woo")
                .build();

        // when
        User update = userService.update(user.getId(), userUpdate);

        // then
        User result = userService.getByEmail(update.getEmail());
        assertThat(result.getNickname()).isEqualTo("woo");
    }

}