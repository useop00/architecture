package hello.architecture.user.service.dto;

import hello.architecture.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreate {

    private final String email;
    private final String password;
    private final String nickname;

    @Builder
    private UserCreate(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
