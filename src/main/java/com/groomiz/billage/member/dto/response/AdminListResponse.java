package com.groomiz.billage.member.dto.response;

import java.util.List;

import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Major;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "담당자 목록 조회 응답 DTO")
public class AdminListResponse {

	@Schema(description = "단과대 이름", example = "공과대학")
	private College college;
	@Schema(description = "단과대 전화번호", example = "02-1234-5678")
	private String collegePhoneNumber;
	@Schema(description = "담당자 목록")
	private List<AdminInfo> admins;

	@Data
	@Schema(description = "담당자 정보")
	public static class AdminInfo {
		@Schema(description = "소속 학과", example = "기계시스템디자인공학과")
		private Major major;
		@Schema(description = "담당자 전화번호", example = "02-2345-6789")
		private String phoneNumber;
	}
}
