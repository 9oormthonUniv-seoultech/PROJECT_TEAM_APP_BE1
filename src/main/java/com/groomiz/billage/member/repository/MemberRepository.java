package com.groomiz.billage.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groomiz.billage.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByStudentNumber(String studentNumber);

	Boolean existsByStudentNumber(String studentNumber);

	List<Member> findAllByIsAdminTrue();
}
