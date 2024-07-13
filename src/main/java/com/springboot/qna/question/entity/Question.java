package com.springboot.qna.question.entity;

import com.springboot.audit.Auditable;
import com.springboot.member.entity.Member;
import com.springboot.order.entity.OrderCoffee;
import com.springboot.qna.answer.entity.Answer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Question extends Auditable {
    /* 질문ID Long
질문등록 날짜 Date > Now로 자동생성
질문 상태 (초기 상태 값은 QUESTION_REGISTERED) > ENUM
QUESTION_REGISTERED- 질문 등록 상태
QUESTION_ANSWERED - 답변 완료 상태
QUESTION_DELETED - 질문 삭제 상태
QUESTION_DEACTIVED - 질문 비활성화 상태: 회원 탈퇴 시, 질문 비활성화 상태
질문 제목 String @NotBlank
질문 내용 (DB에서는 TEXT) @NotBlank
공개여부
PUBLIC - 공개글 상태
 SECRET - 비밀글 상태*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @OneToOne
    @JoinColumn(name = "ANSWER_ID")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionStatus status = QuestionStatus.QUESTION_ANSWERED;

    @Enumerated(EnumType.STRING)
    @Column
    private Visibility visibility = Visibility.PUBLIC;

    @OneToMany(mappedBy = "question")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "question")
    private List<View> views = new ArrayList<>();

//    @Column(nullable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Column(nullable = false, name = "LAST_MODIFIED_AT")
//    private LocalDateTime modifiedAt = LocalDateTime.now();


    public void addMember(Member member) {
        //member의 입장에서도 연결이 필요함
        //member가 가지고 있는 orders(List)에
        //나자신 Member(this)를 추가함
        this.member = member;
        if (!member.getQuestions().contains(this)) {
            member.addQuestion(this);
        }

    }

    public void removeMember(Member member) {
        this.member = null;
        if (member.getQuestions().contains(this)){
            member.removeQuestion(this);
        }
    }


    public void setAnswer(Answer answer) {
        this.answer = answer;
        if (answer.getQuestion() != this) {
            answer.setQuestion(this);
        }
    }

    public void setLike(Like like) {
        likes.add(like);
        if (like.getQuestion() != this) {
            like.setQuestion(this);
        }
    }

    public void setView(View view) {
        views.add(view);
        if (view.getQuestion() != this) {
            view.setQuestion(this);
        }
    }



    public void removeLike(Like like) {
        this.likes.remove(like);
        if (like.getQuestion() == this){
            like.removeQuestion(this);
        }
    }




    public enum QuestionStatus {
        QUESTION_REGISTERED(1, "질문 등록"),
        QUESTION_ANSWERED(2, "답변 완료"),
        QUESTION_DELETED(3, "질문 삭제"),
        //회원 탈퇴 시, 질문 비활성화 상태
        QUESTION_DEACTIVED(4, "질문 비활성화");


        @Getter
        private int stepNumber;

        @Getter
        private String stepDescription;

        QuestionStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }


    }

    public enum Visibility {
        PUBLIC(1, "공개글"),
        SECRET(2, "비밀글");

        @Getter
        private int stepNumber;

        @Getter
        private String stepDescription;

        Visibility(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }

}

