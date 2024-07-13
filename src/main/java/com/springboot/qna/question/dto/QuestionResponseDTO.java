package com.springboot.qna.question.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QuestionResponseDTO {
    private Long questionId;
    private Long memberId;
    private String title;
    private String content;
    private String postDate;
    private String status;
    private String visibility;
}
