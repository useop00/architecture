package hello.architecture.user.controller;

import hello.architecture.mock.TestContainer;
import hello.architecture.user.controller.response.UserResponse;
import hello.architecture.user.domain.Login;
import hello.architecture.user.domain.User;
import hello.architecture.user.domain.UserCreate;
import hello.architecture.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


class UserControllerTest {

    @Test
    void create() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        UserCreate userCreate = UserCreate.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();

        // when
        ResponseEntity<UserResponse> response = testContainer.userController.create(userCreate);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("seop@naver.com");
        assertThat(response.getBody().getNickname()).isEqualTo("seop");
        assertThat(response.getBody().getPassword()).isEqualTo("1234");
    }

    @Test
    void login() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        User loginUser = testContainer.userRepository.save(user);

        // when
        Login login = Login.of(loginUser.getEmail(), loginUser.getPassword());
        ResponseEntity<UserResponse> response = testContainer.userController.login(login);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("seop@naver.com");
        assertThat(response.getBody().getNickname()).isEqualTo("seop");
        assertThat(response.getBody().getPassword()).isEqualTo("1234");

    }

    @Test
    void updateMyInfo() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        User user = User.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        testContainer.userRepository.save(user);

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("woo")
                .build();

        // when
        ResponseEntity<UserResponse> response = testContainer.userController.updateMyInfo(user.getEmail(), userUpdate);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNickname()).isEqualTo("woo");

    }
}