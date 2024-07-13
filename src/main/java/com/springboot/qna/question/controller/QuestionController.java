package com.springboot.qna.question.controller;

import com.springboot.qna.answer.entity.Answer;
import com.springboot.qna.question.dto.LikeDTO;
import com.springboot.qna.question.dto.QuestionPatchDTO;
import com.springboot.qna.question.dto.QuestionPostDTO;
import com.springboot.qna.question.entity.Like;
import com.springboot.qna.question.entity.Question;
import com.springboot.qna.question.mapper.QuestionMapper;
import com.springboot.qna.question.service.QuestionService;
import com.springboot.response.MultiResponseDto;
import com.springboot.response.SingleResponseDto;
import com.springboot.utils.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/v11/questions")
@Validated
@Slf4j
public class QuestionController {

    private final static String QUESTION_DEFAULT_URL = "/v11/questions";

//    Question 서비스
    private final QuestionService questionService;

    //    Question 매퍼
    private  final QuestionMapper mapper;
    //    생성자
    public QuestionController(QuestionService questionService, QuestionMapper mapper) {
        this.questionService = questionService;
        this.mapper = mapper;
    }


//    postQuestion

    @PostMapping
    public ResponseEntity postQuestion(@Valid @RequestBody QuestionPostDTO questionPostDTO){
        Question question =
                questionService.createQuestion(mapper.questionPostDTOToQuestion(questionPostDTO));
        question.setAnswer(new Answer());

        URI location = UriCreator.createUri(QUESTION_DEFAULT_URL, question.getQuestionId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{question-id}")
    public ResponseEntity patchQuestion(@PathVariable("question-id") @Positive long questionId,
                                        @Valid @RequestBody QuestionPatchDTO questionPatchDTO){
        questionPatchDTO.setQuestionId(questionId);
        Question question = questionService.updateQuestion(mapper.questionPatchDTOToQuestion(questionPatchDTO));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.questionToQuestionResponseDTO(question)), HttpStatus.OK);

    }

    //    getQuestion(answer가 포함되어있는)
    @GetMapping("/{question-id}")
    public ResponseEntity getQuestion(@PathVariable("question-id") @Positive long questionId){
        Question question = questionService.findQuestion(questionId);
        //question
        return new ResponseEntity<>(new SingleResponseDto<>(mapper.questionToQuestionResponseDTO(question)), HttpStatus.OK);
    }
    //    getQuestion List
    @GetMapping
    public ResponseEntity getQuestions(@Positive @RequestParam int page, @Positive @RequestParam int size){
        Page<Question> questionPage = questionService.findQuestions(page -1, size);
        List<Question> questions = questionPage.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(mapper.questionsToResponseDTOs(questions), questionPage), HttpStatus.OK);
    }

    //    deleteQuestion> 질문이 사라지면 답변도 사라짐
    @DeleteMapping("/{question-id}")
    public ResponseEntity deleteQuestion(@PathVariable("question-id") @Positive long questionId){
        questionService.deleteQuestion(questionId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //좋아요 추가  post
    @PostMapping("/like")
    public void postLikeToQuestion(@Valid @RequestBody LikeDTO likeDTO){
        Like like =
                questionService.clicklike(mapper.likeDTOToLike(likeDTO));

        new ResponseEntity<>(HttpStatus.OK);
    }

}
