package com.springboot.qna.question.dto;

import com.springboot.qna.question.entity.Question;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class QuestionPostDTO {

    @Positive
    private long memberId;

    @NotBlank(message = "제목은 공백이 아니어야 합니다.")
    private String title;

    @NotBlank(message = "내용은 공백이 아니어야 합니다.")
    private String content;


}
