package hello.architecture.mock;

import hello.architecture.post.controller.PostController;
import hello.architecture.post.controller.port.PostService;
import hello.architecture.post.service.PostServiceImpl;
import hello.architecture.post.service.port.PostRepository;
import hello.architecture.user.controller.UserController;
import hello.architecture.user.controller.port.UserService;
import hello.architecture.user.service.UserServiceImpl;
import hello.architecture.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final PostService postService;
    public final UserService userService;
    public final PostController postController;
    public final UserController userController;

    @Builder
    public TestContainer() {
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.postService = PostServiceImpl.forTest(postRepository, userRepository);
        this.userService = UserServiceImpl.forTest(userRepository);
        this.postController = PostController.builder()
                .postService(postService)
                .build();
        this.userController = UserController.builder()
                .userService(userService)
                .build();
    }
}
