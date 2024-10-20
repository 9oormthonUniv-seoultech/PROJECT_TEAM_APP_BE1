package com.groomiz.billage.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "비밀번호 수정 요청 DTO")
public class PasswordRequest {

	@NotNull
	@Schema(description = "기존 비밀번호", example = "password1234!")
	private String oldPassword;

	@NotNull
	@Schema(description = "새로운 비밀번호", example = "newpassword1234!")
	private String newPassword;
}
