package hello.architecture.user.controller.port;

import hello.architecture.user.domain.Login;
import hello.architecture.user.domain.User;
import hello.architecture.user.domain.UserCreate;
import hello.architecture.user.domain.UserUpdate;

public interface UserService {

    User create(UserCreate request);

    User getByEmail(String email);

    User update(long id, UserUpdate update);

    User login(Login login);
}
