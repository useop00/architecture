package hello.architecture.user.service.port;

import hello.architecture.user.infrastructure.UserEntity;

public interface UserRepository {

    UserEntity save(UserEntity user);

    UserEntity findById(Long id);

    UserEntity findByEmail(String email);
}
