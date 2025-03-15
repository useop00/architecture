package hello.architecture.user.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void create() throws Exception {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();

        // when
        User user = User.from(userCreate);
    
        // then
        assertThat(user.getEmail()).isEqualTo("seop@naver.com");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getNickname()).isEqualTo("seop");
    }

    @Test
    void update() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .email("seop@naver.com")
                .password("1234")
                .nickname("seop")
                .build();
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("woo")
                .build();

        // when
        user = user.update(userUpdate);

        // then
        assertThat(user.getNickname()).isEqualTo("woo");
    }

}