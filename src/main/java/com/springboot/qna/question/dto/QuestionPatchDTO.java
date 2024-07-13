package com.springboot.qna.question.dto;

import com.springboot.qna.question.entity.Question;
import com.springboot.validator.NotSpace;
import lombok.Getter;
import lombok.Setter;

@Getter
public class QuestionPatchDTO {

    @Setter
    private Long questionId;

    @NotSpace(message = "제목은 공백이 아니어야 합니다")
    private String title;

    @NotSpace(message = "내용은 공백이 아니어야 합니다")
    private String content;

    private Question.QuestionStatus questionStatus;

    private Question.Visibility visibility;
}
