package com.groomiz.billage.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groomiz.billage.member.dto.response.AdminListResponse;
import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Major;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.entity.Role;
import com.groomiz.billage.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AdminServiceTest {

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private AdminService adminService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testFindAllAdminInfo() {
		// Given: Mock data for admins
		Member admin1 = Member.builder()
			.username("admin1")
			.phoneNumber("0212345678")
			.role(Role.ADMIN)
			.college(College.ENGINEER)
			.major(Major.MSD)
			.isAdmin(true)
			.build();

		Member admin2 = Member.builder()
			.username("admin2")
			.phoneNumber("0212345679")
			.role(Role.ADMIN)
			.college(College.ENGINEER)
			.major(Major.EE)
			.isAdmin(true)
			.build();

		Member admin3 = Member.builder()
			.username("admin3")
			.phoneNumber("0212345680")
			.role(Role.ADMIN)
			.college(College.ICE)
			.major(Major.CS)
			.isAdmin(true)
			.build();

		// When
		when(memberRepository.findAllByIsAdminTrue()).thenReturn(Arrays.asList(admin1, admin2, admin3));

		// Then:
		List<AdminListResponse> result = adminService.findAllAdminInfo();
		System.out.println("Result: " + result);


		assertThat(result).hasSize(2);


		AdminListResponse engineerCollege = result.stream()
			.filter(response -> response.getCollege().getName().equals("공과대학"))
			.findFirst()
			.orElse(null);

		assertThat(engineerCollege).isNotNull();
		assertThat(engineerCollege.getCollege().getName()).isEqualTo("공과대학");
		assertThat(engineerCollege.getCollegePhoneNumber()).isEqualTo("0212345678");
		assertThat(engineerCollege.getAdmins()).hasSize(2);

		// 공대 정보 확인
		AdminListResponse.AdminInfo firstEngineerAdmin = engineerCollege.getAdmins().get(0);
		assertThat(firstEngineerAdmin.getMajor().getName()).isEqualTo("기계시스템디자인공학과");
		assertThat(firstEngineerAdmin.getPhoneNumber()).isEqualTo("0212345678");

		AdminListResponse.AdminInfo secondEngineerAdmin = engineerCollege.getAdmins().get(1);
		assertThat(secondEngineerAdmin.getMajor().getName()).isEqualTo("전자공학과");
		assertThat(secondEngineerAdmin.getPhoneNumber()).isEqualTo("0212345679");

		// 정통대 정보 확인
		AdminListResponse iceCollege = result.stream()
			.filter(response -> response.getCollege().getName().equals("정보통신대학"))
			.findFirst()
			.orElse(null);

		assertThat(iceCollege).isNotNull();
		assertThat(iceCollege.getCollege()).isEqualTo("정보통신대학");
		assertThat(iceCollege.getCollegePhoneNumber()).isEqualTo("0212345680");
		assertThat(iceCollege.getAdmins()).hasSize(1);


		AdminListResponse.AdminInfo iceAdmin = iceCollege.getAdmins().get(0);
		assertThat(iceAdmin.getMajor().getName()).isEqualTo("컴퓨터공학과");
		assertThat(iceAdmin.getPhoneNumber()).isEqualTo("0212345680");
	}
}
