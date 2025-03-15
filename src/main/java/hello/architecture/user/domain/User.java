package hello.architecture.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final String password;
    private final String nickname;

    @Builder
    private User(Long id, String email, String password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public static User from(UserCreate request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .build();

    }

    public User update(UserUpdate userUpdate) {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .nickname(userUpdate.getNickname())
                .build();
    }

}
