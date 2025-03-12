package hello.architecture.user.service;

import hello.architecture.common.exception.UserNotFoundException;
import hello.architecture.user.controller.response.UserResponse;
import hello.architecture.user.domain.UserJpaRepository;
import hello.architecture.user.infrastructure.UserEntity;
import hello.architecture.user.service.dto.UserCreate;
import hello.architecture.user.service.dto.UserUpdate;
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
    private UserJpaRepository userJpaRepository;

    @Test
    void create() throws Exception {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();

        // when
        UserResponse result = userService.create(userCreate);

        // then
        assertThat(result.getEmail()).isEqualTo("seop@naver.com");
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getNickname()).isEqualTo("seop");
    }

    @Test
    void getByEmail() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userJpaRepository.save(user);

        // when
        UserResponse result = userService.getByEmail(user.getEmail());

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
        UserEntity user = UserEntity.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userJpaRepository.save(user);

        // when
        UserResponse result = userService.login(user.getEmail(), user.getPassword());

        // then
        assertThat(result.getNickname()).isEqualTo("seop");
    }

    @Test
    void passwordDoesNotMatch() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userJpaRepository.save(user);

        //expect
        assertThatThrownBy(() -> userService.login(user.getEmail(), "3333")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        userJpaRepository.save(user);

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("woo")
                .build();

        // when
        userService.update(user.getId(), userUpdate);

        // then
        assertThat(user.getNickname()).isEqualTo("woo");
    }

}