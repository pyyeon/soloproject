package com.springboot.qna.answer.entity;

import com.springboot.audit.Auditable;
import com.springboot.member.entity.Member;
import com.springboot.qna.question.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Answer extends Auditable {

    @Id
    private Long answerId;


    @OneToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @NotBlank
    private String content;


    public void setQuestion(Question question) {
        this.question = question;
        if (question.getAnswer() != this) {
            question.setAnswer(this);
        }
    }

    public void addMember(Member member) {
        //member의 입장에서도 연결이 필요함
        //member가 가지고 있는 orders(List)에
        //나자신 Member(this)를 추가함
        this.member = member;
        if (!member.getAnswers().contains(this)) {
            member.addAnswer(this);
        }
    }

    public void removeMember(Member member) {
        this.member = null;
        if (member.getAnswers().contains(this)){
            member.removeAnswer(this);
        }
    }

}
