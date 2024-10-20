package com.groomiz.billage.building.exception;

import static com.groomiz.billage.global.consts.BillageStatic.*;

import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.dto.ErrorReason;
import com.groomiz.billage.global.exception.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuildingErrorCode implements BaseErrorCode {

	@ExplainError("존재하지 않는 건물일 경우 발생하는 오류입니다.")
	BUILDING_NOT_FOUND(NOT_FOUND, "BUILDING_404_1", "해당 건물이 존재하지 않습니다."),

	@ExplainError("존재하지 않는 층일 경우 발생하는 오류입니다.")
	FLOOR_NOT_FOUND(NOT_FOUND, "BUILDING_404_2", "해당 층이 존재하지 않습니다."),

	@ExplainError("건물이 틀린 경우 발생하는 오류입니다.")
	INVALID_BUILDING(BAD_REQUEST, "BUILDING_400_1", "잘못된 건물 정보입니다."),

	@ExplainError("층과 건물이 맞지 않는 경우 발생하는 오류입니다.")
	FLOOR_BUILDING_MISMATCH(BAD_REQUEST, "BUILDING_400_2", "층과 건물이 일치하지 않습니다."),

	@ExplainError("혼잡도 레벨이 올바르지 않을 때 발생하는 오류입니다.")
	INVALID_CONGESTION_LEVEL(BAD_REQUEST, "BUILDING_400_3", "혼잡도가 올바르지 않습니다. 허용되는 값은 'LOW', 'AVG', 'HIGH'입니다.");


	private final Integer status;
	private final String code;
	private final String reason;

	@Override
	public ErrorReason getErrorReason() {
		return ErrorReason.builder().reason(reason).code(code).status(status).build();
	}

	@Override
	public String getExplainError() {
		return this.reason;
	}
}
