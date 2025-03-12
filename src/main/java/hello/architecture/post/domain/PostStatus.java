package hello.architecture.post.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostStatus {
    PRIVATE("비공개"),
    PUBLIC("공개");

    private final String description;
}

