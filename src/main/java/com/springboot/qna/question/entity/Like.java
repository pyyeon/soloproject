package com.springboot.qna.question.entity;

import com.springboot.audit.Auditable;
import com.springboot.member.entity.Member;
import com.springboot.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "LIKES")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_ID")
    //스위치
    private Member member;


    public void setQuestion(Question question) {
        this.question = question;
        if (question.getLikes().contains(this)) {
            question.setLike(this);
        }
    }

    public void removeQuestion(Question question){
        this.question = null;
        if (question.getLikes().contains(this)){
            question.removeLike(this);
        }
    }



    public void removeMember(Member member){
        this.member = null;
    }


}
