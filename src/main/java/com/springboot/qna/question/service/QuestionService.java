package com.springboot.qna.question.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.qna.question.entity.Like;
import com.springboot.qna.question.entity.Question;
import com.springboot.qna.question.repository.LikeRepository;
import com.springboot.qna.question.repository.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final LikeRepository likeRepository;
    private final MemberService memberService;


    public QuestionService(QuestionRepository questionRepository, LikeRepository likeRepository, MemberService memberService) {
        this.questionRepository = questionRepository;
        this.likeRepository = likeRepository;
        this.memberService = memberService;
    }


    public Question createQuestion(Question question) {
        memberService.findVerifiedMember(question.getMember().getMemberId());
        return questionRepository.save(question);

    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Question updateQuestion(Question question) {

        Question findQuestion = findQuestion(question.getQuestionId());

        Optional.ofNullable(question.getTitle())
                .ifPresent(title -> findQuestion.setTitle(title));
        Optional.ofNullable(question.getContent())
                .ifPresent(content -> findQuestion.setContent(content));
        Optional.ofNullable(question.getStatus())
                .ifPresent(questionStatus -> findQuestion.setStatus(questionStatus));
        Optional.ofNullable(question.getVisibility())
                .ifPresent(visibility -> findQuestion.setVisibility(visibility));

        return questionRepository.save(question);
    }

    public Question findQuestion(long questionId) {

        return findVerifiedQuestion(questionId);

    }

    public Page<Question> findQuestions(int page, int size) {
        return questionRepository.findAll(PageRequest.of(page, size, Sort.by("questionId").descending()));
        //
    }

    public void deleteQuestion(long questionId) {
        Question findQuestion = findVerifiedQuestion(questionId);
        int step = findQuestion.getStatus().getStepNumber();

        if (step >= 2) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_QUESTION);
        }
        findQuestion.setStatus(Question.QuestionStatus.QUESTION_DELETED);


        questionRepository.save(findQuestion);
    }

    public Question findVerifiedQuestion(Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        return optionalQuestion.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
    }

    public void isExistQuestion() {

    }


    public void isPublic() {

    }


    public Like clicklike(Like like) {
        //같은 멤버의 아이디의 좋아요가
        //있으면  > 좋아요 삭제
        //없으면 > 좋아요 생성!

        Member findMember = memberService.findVerifiedMember(like.getMember().getMemberId());
        Question findQuestion = findVerifiedQuestion(like.getQuestion().getQuestionId());

        Optional<Like> opLike = likeRepository.findByMemberAndQuestion(findMember, findQuestion);
        if (opLike.isPresent()){
            //좋아요 누르깅
           Like findLike = opLike.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CANNOT_LIKE));
           findLike.removeQuestion(findQuestion);
           findLike.removeMember(findMember);
           likeRepository.delete(findLike);
        }else {
            // quesion에서도 지우고 like에서도 like를 지워야함다
            Like addLike = new Like();
            addLike.setMember(findMember);
            addLike.setQuestion(findQuestion);
            return likeRepository.save(addLike);
        }
        return null;
    }




    //양쪽에서 지울 수 있는 메서드
}
