package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.springboot.member.entity.Member.MemberStatus.MEMBER_ACTIVE;

@Service
public class MemberService {


    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        // TODO should business logic
        // throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        verifyExistsEmail(member.getEmail());
        return memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        // TODO should business logic
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        Member findMember = findVerifiedMember(member.getMemberId());

//    if (member.getName() != null){
//        findMember.setName(member.getName());
//    }
//    if (member.getPhone() != null){
//        findMember.setPhone(member.getPhone());
//    }
        Optional.ofNullable(member.getName())
                .ifPresent(name -> findMember.setName(name));

        Optional.ofNullable(member.getPhone())
                .ifPresent(phone -> findMember.setPhone(phone));

        Optional.ofNullable(member.getMemberStatus())
                .ifPresent(memberStatus -> findMember.setMemberStatus(memberStatus));

        findMember.setModifiedAt(LocalDateTime.now());
        return memberRepository.save(findMember);
    }

    public Member findMember(long memberId) {
        // TODO should business logic
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        return findVerifiedMember(memberId);

    }

    public Page<Member> findMembers(int page, int size) {
        // TODO should business logic
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    return memberRepository.findAll(PageRequest.of(page, size, Sort.by("memberId").descending()));

    }

    public void deleteMember(long memberId) {
        // TODO should business logic
        Member findMember = findVerifiedMember(memberId);
        memberRepository.delete(findMember);
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    }

    public void quitMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);

        if (findMember.getMemberStatus() != MEMBER_ACTIVE) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_MEMBER_STATUS);
        }
        findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);

        findMember.setModifiedAt(LocalDateTime.now());
        memberRepository.save(findMember);
    }


    public void sleepMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);

        if (findMember.getMemberStatus() != MEMBER_ACTIVE) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_MEMBER_STATUS);
        }

        findMember.setMemberStatus(Member.MemberStatus.MEMBER_SLEEP);
        findMember.setModifiedAt(LocalDateTime.now());
        memberRepository.save(findMember);
    }


    public Member findVerifiedMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
}
