package com.groomiz.billage.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.groomiz.billage.auth.dto.RegisterRequest;
import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.auth.jwt.JwtUtil;
import com.groomiz.billage.member.dto.response.MemberInfoResponse;
import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Major;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.entity.Role;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	//Todo : 약관동의도 넣어야 함
	public void register(RegisterRequest registerRequest) {
		String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
		Member member = Member.builder()
			.username(registerRequest.getName())
			.password("{bcrypt}" + encodedPassword)
			.phoneNumber(registerRequest.getPhoneNumber())
			.role(Role.ADMIN)
			.studentNumber(registerRequest.getStudentNumber())
			.isAdmin(true)
			.college(College.fromName(registerRequest.getCollege()))
			.major(Major.fromName(registerRequest.getMajor()))
			.studentEmail(registerRequest.getStudentEmail())
			.build();
		memberRepository.save(member);

	}
	public MemberInfoResponse getMemberInfo(String username) {

		// 회원 정보 조회
		Member member = memberRepository.findByUsername(username)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));


		// 응답 DTO 생성
		// 예약 횟수 구현 안함
		return MemberInfoResponse.builder()
			.studentNumber(member.getStudentNumber())
			.name(member.getUsername())
			.phoneNumber(member.getPhoneNumber())
			.college(member.getCollege())
			.major(member.getMajor())
			.email(member.getStudentEmail())
			.reservationCount(3)
			.build();
	}

	public boolean isExists(String studentNumber) {
		return memberRepository.existsByStudentNumber(studentNumber);
	}
}
