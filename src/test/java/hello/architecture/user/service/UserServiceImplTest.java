package hello.architecture.user.service;

import hello.architecture.common.exception.UserNotFoundException;
import hello.architecture.mock.FakeUserRepository;
import hello.architecture.user.domain.Login;
import hello.architecture.user.domain.User;
import hello.architecture.user.domain.UserCreate;
import hello.architecture.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceImplTest {

    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void initialize() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userServiceImpl = UserServiceImpl.forTest(fakeUserRepository);

        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build());
    }

    @Test
    void create() throws Exception {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();

        // when
        User result = userServiceImpl.create(userCreate);

        // then
        assertThat(result.getEmail()).isEqualTo("seop@naver.com");
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getNickname()).isEqualTo("seop");
    }

    @Test
    void getByEmail() throws Exception {
        // given
        String email = "seop@naver.com";

        // when
        User result = userServiceImpl.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("seop");
    }

    @Test
    void getByEmailToFail() throws Exception {
        // given
        String email = "";

        //expect
        assertThatThrownBy(() -> userServiceImpl.getByEmail(email)
        ).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void login() throws Exception {
        // given
        String email = "seop@naver.com";
        String password = "1234";

        // when
        User result = userServiceImpl.login(Login.of(email, password));

        // then
        assertThat(result.getNickname()).isEqualTo("seop");
        assertThat(result.getEmail()).isEqualTo("seop@naver.com");
        assertThat(result.getPassword()).isEqualTo("1234");
    }

    @Test
    void passwordDoesNotMatch() throws Exception {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User user = userServiceImpl.create(userCreate);

        //expect
        assertThatThrownBy(() -> userServiceImpl.login(Login.of(user.getEmail(), "3333"))
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
        User user = userServiceImpl.create(userCreate);
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("woo")
                .build();

        // when
        User update = userServiceImpl.update(user.getId(), userUpdate);

        // then
        User result = userServiceImpl.getByEmail(update.getEmail());
        assertThat(result.getNickname()).isEqualTo("woo");
    }

}