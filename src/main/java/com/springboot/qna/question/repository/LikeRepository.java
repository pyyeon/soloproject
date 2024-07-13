package com.springboot.qna.question.repository;

import com.springboot.member.entity.Member;
import com.springboot.qna.question.entity.Like;
import com.springboot.qna.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberAndQuestion(Member member, Question question);
}
