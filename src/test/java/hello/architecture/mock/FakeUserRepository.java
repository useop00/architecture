package hello.architecture.mock;

import hello.architecture.user.domain.User;
import hello.architecture.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {

    private final AtomicLong autoIncrementId = new AtomicLong(1);
    private final List<User> data = new ArrayList<>();

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            User newUser = User.builder()
                    .id(autoIncrementId.getAndIncrement())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .nickname(user.getNickname())
                    .build();
            data.add(newUser);
            return newUser;
        } else {
            data.removeIf(u -> Objects.equals(u.getId(), user.getId()));
            data.add(user);
            return user;
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return data.stream()
                .filter(u -> u.getId().equals(id))
                .findAny();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return data.stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny();
    }
}
