package com.springboot.member.entity;

import com.springboot.order.entity.Order;
import com.springboot.qna.answer.entity.Answer;
import com.springboot.qna.question.entity.Like;
import com.springboot.qna.question.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 13, nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();


    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE)
    private List<Answer> answers = new ArrayList<>();




    // TODO 추가 된 부분
    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }
    public void addOrder(Order order){
        orders.add(order);
        if(order.getMember() != this){
            order.addMember(this);
        }
        //만든 객체의 오더에 자기 자신이 없으면 애드멤버로 자기 자신을 넣음
        //그러면 오더 클래스에서 멤버의 오더스에 멤버를 추가함 > 그러면 다시 멤버들어와서 추가
        //> 순환참조
        //하나라도 올라가 있다면 멤버 클래스에서 오더스에 오더를 추가
        //> 객체의 오더에  자기 자신이 없더면 추가 > 오더 클래스에서 오더 있는지 확인 > 있음 > if 조건 충족 못함 >If안에 실행 안 함
    }

    public void addQuestion(Question question){
        questions.add(question);
        if(question.getMember() != this){
            question.addMember(this);
        }
        //만든 객체의 오더에 자기 자신이 없으면 애드멤버로 자기 자신을 넣음
        //그러면 오더 클래스에서 멤버의 오더스에 멤버를 추가함 > 그러면 다시 멤버들어와서 추가
        //> 순환참조
        //하나라도 올라가 있다면 멤버 클래스에서 오더스에 오더를 추가
        //> 객체의 오더에  자기 자신이 없더면 추가 > 오더 클래스에서 오더 있는지 확인 > 있음 > if 조건 충족 못함 >If안에 실행 안 함
    }
    public void removeQuestion(Question question) {
        this.questions.remove(question);
        if (question.getMember() == this){
            question.removeMember(this);
        }
    }

    public void  addAnswer(Answer answer){
        answers.add(answer);
        if(answer.getMember() != this){
            answer.addMember(this);
        }
    }

    public void removeAnswer(Answer answer) {
        this.answers.remove(answer);
        if (answer.getMember() == this){
            answer.removeMember(this);
        }
    }


}
