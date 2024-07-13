package com.springboot.qna.question.entity;

import com.springboot.audit.Auditable;
import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long viewId;


    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;



    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    //스위치
    private Member member;


    public void setQuestion(Question question){
        this.question = question;
        if (question.getViews().contains(this)){
            question.setView(this);
        }
    }




}
