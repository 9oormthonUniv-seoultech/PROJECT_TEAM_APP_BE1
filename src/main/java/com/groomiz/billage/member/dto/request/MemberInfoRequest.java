package com.groomiz.billage.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Schema(description = "회원 정보 수정 요청 DTO")
public class MemberInfoRequest {

	@NotNull
	@Schema(description = "전화번호", example = "010-1234-5678")
	private String phoneNumber;

	@NotNull
	@Schema(description = "이메일", example = "asdf1234@gmail.com")
	private String email;

}
