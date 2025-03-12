package hello.architecture.user.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdate {
    private final String nickname;

    @Builder
    private UserUpdate(String nickname) {
        this.nickname = nickname;
    }
}
