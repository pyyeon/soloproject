package com.springboot.member.repository;

import com.springboot.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {//(1) 수정된 부분
    Optional<Member> findByEmail(String email);

}

