package hello.architecture.user.service;

import hello.architecture.common.exception.UserNotFoundException;
import hello.architecture.user.controller.response.UserResponse;
import hello.architecture.user.domain.UserJpaRepository;
import hello.architecture.user.infrastructure.UserEntity;
import hello.architecture.user.service.dto.UserCreate;
import hello.architecture.user.service.dto.UserUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;

    @Transactional
    public UserResponse create(UserCreate create) {
        UserEntity user = create.toEntity();
        userJpaRepository.save(user);

        return UserResponse.of(user);
    }

    public UserResponse getByEmail(String email) {
        UserEntity user = userJpaRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse.of(user);
    }

    @Transactional
    public void update(long id, UserUpdate update) {
        UserEntity user = userJpaRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user.update(update.getNickname());

        UserResponse.of(user);
    }

    @Transactional
    public UserResponse login(String email, String password) {
        UserEntity user = userJpaRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        if (isNotEqualsPassword(password, user)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return UserResponse.of(user);
    }

    private static boolean isNotEqualsPassword(String password, UserEntity user) {
        return !user.getPassword().equals(password);
    }
}
