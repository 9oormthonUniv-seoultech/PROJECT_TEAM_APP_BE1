package com.groomiz.billage.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {


	Optional<Member> findByUsername(String username);

	Optional<Member> findByStudentNumber(String studentNumber);

	Boolean existsByStudentNumber(String studentNumber);

}
