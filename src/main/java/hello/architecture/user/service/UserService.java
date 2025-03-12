package hello.architecture.user.service;

import hello.architecture.user.controller.response.UserResponse;
import hello.architecture.user.infrastructure.UserEntity;
import hello.architecture.user.service.dto.UserCreate;
import hello.architecture.user.service.dto.UserUpdate;
import hello.architecture.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(UserCreate create) {
        UserEntity user = create.toEntity();
        userRepository.save(user);

        return UserResponse.of(user);
    }

    public UserResponse getByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        return UserResponse.of(user);
    }

    @Transactional
    public void update(long id, UserUpdate update) {
        UserEntity user = userRepository.findById(id);
        user.update(update.getNickname());

        UserResponse.of(user);
    }

    @Transactional
    public UserResponse login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);

        if (isNotEqualsPassword(password, user)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return UserResponse.of(user);
    }

    private static boolean isNotEqualsPassword(String password, UserEntity user) {
        return !user.getPassword().equals(password);
    }
}
