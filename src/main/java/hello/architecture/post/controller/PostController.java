package hello.architecture.post.controller;

import hello.architecture.post.controller.response.PostResponse;
import hello.architecture.post.domain.Post;
import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.service.PostService;
import hello.architecture.post.service.dto.PostCreate;
import hello.architecture.post.service.dto.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> write(@RequestBody PostCreate postCreate) {
        LocalDateTime createAt = LocalDateTime.now();
        Post response = postService.write(postCreate, createAt);
        return ResponseEntity
                .status(CREATED)
                .body(PostResponse.of(response));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> findAll() {
        List<Post> responses = postService.findAll();
        return ResponseEntity
                .ok()
                .body(PostResponse.of(responses));
    }


    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        Post response = postService.findById(id);
        return ResponseEntity
                .ok()
                .body(PostResponse.of(response));
    }

    @GetMapping("/posts/{writerId}/{status}")
    public ResponseEntity<List<PostResponse>> findByWriterIdAndStatus(@PathVariable Long writerId, @PathVariable PostStatus status) {
        List<Post> responses = postService.findByWriterIdAndStatus(writerId, status);
        return ResponseEntity
                .ok()
                .body(PostResponse.of(responses));
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable Long id, @RequestBody PostUpdate postUpdate) {
        LocalDateTime modifyAt = LocalDateTime.now();
        Post response = postService.update(id, postUpdate, modifyAt);
        return ResponseEntity
                .ok()
                .body(PostResponse.of(response));
    }


}
