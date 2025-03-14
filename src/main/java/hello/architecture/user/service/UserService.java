package hello.architecture.user.service;

import hello.architecture.common.exception.UserNotFoundException;
import hello.architecture.user.domain.User;
import hello.architecture.user.service.dto.Login;
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
    public User create(UserCreate request) {
        User user = User.from(request);
        user = userRepository.save(user);

        return user;
    }

    public User getByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new UserNotFoundException();
        }
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User update(long id, UserUpdate update) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user = user.update(update);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User login(Login login) {
        User user = getByEmail(login.getEmail());
        if (!user.getPassword().equals(login.getPassword())) {
            throw new UserNotFoundException();
        }
        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }
}
