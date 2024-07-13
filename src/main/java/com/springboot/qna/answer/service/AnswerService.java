package com.springboot.qna.answer.service;

import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.qna.answer.repository.AnswerRepository;
import com.springboot.qna.question.repository.LikeRepository;
import com.springboot.qna.question.repository.QuestionRepository;

public class AnswerService {
    private final AnswerRepository answerRepository;

    private final MemberService memberService;

}
