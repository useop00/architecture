package hello.architecture.user.domain;

import lombok.Getter;

@Getter
public class Login {
    private final String email;
    private final String password;

    private Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Login of(String email, String password) {
        return new Login(email, password);
    }
}
