package com.groomiz.billage.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Major;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.entity.Role;
import com.groomiz.billage.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional  // 테스트 종료 시 데이터 롤백
public class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	public void testInsertAndRetrieveMembers() {
		// 테스트용 더미 데이터 삽입
		Member admin1 = Member.builder()
			.username("admin1")
			.password("password123")
			.role(Role.ADMIN)
			.phoneNumber("0212345678")
			.college(College.ENGINEER)
			.major(Major.MSD)
			.isAdmin(true)
			.isValid(true)
			.studentEmail("admin1@univ.edu")
			.verificationCode(111111)
			.build();

		Member admin2 = Member.builder()
			.username("admin2")
			.password("password123")
			.role(Role.ADMIN)
			.phoneNumber("0223456789")
			.college(College.ICE)
			.major(Major.EE)
			.isAdmin(true)
			.isValid(true)
			.studentEmail("admin2@univ.edu")
			.verificationCode(222222)
			.build();

		memberRepository.save(admin1);
		memberRepository.save(admin2);

		// 데이터베이스에서 데이터 조회
		List<Member> admins = memberRepository.findAll();

		// 데이터가 2개 삽입되었는지 확인
		assertThat(admins).hasSize(2);
		assertThat(admins.get(0).getUsername()).isEqualTo("admin1");
		assertThat(admins.get(1).getUsername()).isEqualTo("admin2");

		System.out.println("테스트 더미 데이터 삽입 및 검증 완료!");
	}
}
