package com.groomiz.billage.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.groomiz.billage.member.dto.response.AdminListResponse;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final MemberRepository memberRepository;

	public List<AdminListResponse> findAllAdminInfo() {

		// Get all members with admin privileges
		List<Member> admins = memberRepository.findAllByIsAdminTrue();

		if (admins.isEmpty()) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
		}

		return admins.stream()
			.collect(Collectors.groupingBy(Member::getCollege))  // Group by College entity
			.entrySet().stream()
			.map(entry -> {
				AdminListResponse response = new AdminListResponse();

				// 직렬화 시 College 객체가 getName()을 사용해 한글 이름으로 변환됨
				response.setCollege(entry.getKey());  // College 객체 설정

				// 단과대 전화번호를 설정 (첫 번째 멤버의 전화번호 사용)
				String collegePhoneNumber = entry.getValue().get(0).getPhoneNumber();
				response.setCollegePhoneNumber(collegePhoneNumber);

				// 담당자 정보 리스트 생성
				List<AdminListResponse.AdminInfo> adminInfos = entry.getValue().stream()
					.map(admin -> {
						AdminListResponse.AdminInfo adminInfo = new AdminListResponse.AdminInfo();

						// 직렬화 시 Major 객체가 getName()을 사용해 한글 이름으로 변환됨
						adminInfo.setMajor(admin.getMajor());  // Major 객체 설정
						adminInfo.setPhoneNumber(admin.getPhoneNumber());  // 담당자 전화번호 설정
						return adminInfo;
					})
					.collect(Collectors.toList());

				response.setAdmins(adminInfos); // 담당자 리스트 설정
				return response;
			})
			.collect(Collectors.toList()); // 결과 리스트 반환
	}
}
