package hello.architecture.post.controller;

import hello.architecture.post.controller.response.PostResponse;
import hello.architecture.post.domain.PostJpaRepository;
import hello.architecture.post.domain.PostStatus;
import hello.architecture.post.service.PostService;
import hello.architecture.post.service.dto.PostCreate;
import hello.architecture.post.service.dto.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> write(@RequestBody PostCreate postCreate) {
        PostResponse response = postService.write(postCreate);
        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> findAll() {
        List<PostResponse> responses = postService.findAll();
        return ResponseEntity
                .ok()
                .body(responses);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        PostResponse response = postService.findById(id);
        return ResponseEntity
                .ok()
                .body(response);
    }

    @GetMapping("/posts/{writerId}/{status}")
    public ResponseEntity<List<PostResponse>> findByWriterIdAndStatus(@PathVariable Long writerId, @PathVariable PostStatus status) {
        List<PostResponse> responses = postService.findByWriterIdAndStatus(writerId, status);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable Long id, @RequestBody PostUpdate postUpdate) {
        PostResponse response = postService.update(id, postUpdate);
        return ResponseEntity.ok(response);
    }


}
