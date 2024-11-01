package com.groomiz.billage.member.service;

import java.util.List;
import java.util.regex.Pattern;
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

		List<Member> admins = memberRepository.findAllByIsAdminTrue();

		if (admins.isEmpty()) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
		}

		return admins.stream()
			.collect(Collectors.groupingBy(Member::getCollege))
			.entrySet().stream()
			.map(entry -> {
				AdminListResponse response = new AdminListResponse();

				response.setCollege(entry.getKey());

				String collegePhoneNumber = entry.getValue().get(0).getPhoneNumber();
				response.setCollegePhoneNumber(collegePhoneNumber);

				// 전화번호 형식 확인
				if (!isValidPhoneNumber(collegePhoneNumber)) {
					throw new MemberException(MemberErrorCode.INVALID_PHONE_NUMBER);
				}

				// 담당자 정보 리스트 생성
				List<AdminListResponse.AdminInfo> adminInfos = entry.getValue().stream()
					.map(admin -> {
						AdminListResponse.AdminInfo adminInfo = new AdminListResponse.AdminInfo();

						adminInfo.setMajor(admin.getMajor());  // Major 객체 설정
						adminInfo.setPhoneNumber(admin.getPhoneNumber());  // 담당자 전화번호 설정
						return adminInfo;
					})
					.collect(Collectors.toList());

				response.setAdmins(adminInfos);
				return response;
			})
			.collect(Collectors.toList());
	}

	// 전화번호 형식 확인 메서드
	private boolean isValidPhoneNumber(String phoneNumber) {
		String regex = "^010-\\d{4}-\\d{4}$";
		return Pattern.matches(regex, phoneNumber);
	}

}
