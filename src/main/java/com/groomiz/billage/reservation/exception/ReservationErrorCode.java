package com.groomiz.billage.reservation.exception;

import static com.groomiz.billage.global.consts.BillageStatic.*;

import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.dto.ErrorReason;
import com.groomiz.billage.global.exception.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationErrorCode implements BaseErrorCode {

	@ExplainError("예약 날짜가 과거인 경우 발생하는 오류입니다.")
	PAST_DATE_RESERVATION(BAD_REQUEST, "RESERVATION_400_1", "예약 날짜가 과거일 수 없습니다."),

	@ExplainError("예약 날짜가 한달 이후인 경우 발생하는 오류입니다.")
	FUTURE_DATE_LIMIT_EXCEEDED(BAD_REQUEST, "RESERVATION_400_2", "예약 날짜는 한달 이후일 수 없습니다."),

	@ExplainError("인원이 음수인 경우 발생하는 오류입니다.")
	NEGATIVE_PARTICIPANTS(BAD_REQUEST, "RESERVATION_400_3", "인원이 음수일 수 없습니다."),

	@ExplainError("예약 아이디가 존재하지 않는 경우 발생하는 오류입니다.")
	RESERVATION_NOT_FOUND(NOT_FOUND, "RESERVATION_404_1", "해당 예약이 존재하지 않습니다."),

	@ExplainError("이미 만료된 예약인 경우 발생하는 오류입니다.")
	RESERVATION_EXPIRED(BAD_REQUEST, "RESERVATION_400_4", "이미 만료된 예약입니다."),

	@ExplainError("유저와 강의실 예약자가 다른 경우 발생하는 오류입니다.")
	USER_RESERVATION_MISMATCH(FORBIDDEN, "RESERVATION_403_1", "예약자가 유저와 일치하지 않습니다."),

	@ExplainError("예약이 이미 승인된 경우 발생하는 오류입니다.")
	RESERVATION_ALREADY_APPROVED(BAD_REQUEST, "RESERVATION_400_5", "이미 승인된 예약입니다."),

	@ExplainError("예약이 이미 거절된 경우 발생하는 오류입니다.")
	RESERVATION_ALREADY_REJECTED(BAD_REQUEST, "RESERVATION_400_6", "이미 거절된 예약입니다."),

	@ExplainError("예약이 이미 삭제된 경우 발생하는 오류입니다.")
	RESERVATION_ALREADY_DELETED(BAD_REQUEST, "RESERVATION_400_7", "이미 삭제된 예약입니다."),

	@ExplainError("예약 시간이 24시 형식이 아닌 경우 발생하는 오류입니다.")
	INVALID_RESERVATION_TIME(BAD_REQUEST, "RESERVATION_400_8", "예약 시간은 24시 형식이어야 합니다."),

	@ExplainError("예약 인원이 최대 인원을 초과한 경우 발생하는 오류입니다.")
	EXCEED_MAX_PARTICIPANTS(BAD_REQUEST, "RESERVATION_400_9", "예약 인원이 최대 인원을 초과했습니다."),

	@ExplainError("예약 목적이 잘못된 형식인 경우에 발생하는 오류입니다.")
	INVALID_RESERVATION_PURPOSE(BAD_REQUEST, "RESERVATION_400_10", "예약 목적이 잘못된 형식입니다."),

	@ExplainError("예약 목적이 기타인데, 기타에 목적이 없는 경우 발생하는 오류입니다.")
	NO_PURPOSE_IN_ETC(BAD_REQUEST, "RESERVATION_400_11", "기타 목적에 목적이 없습니다."),

	@ExplainError("시작 시간이 종료 시간보다 뒤일 경우 발생하는 오류입니다.")
	START_TIME_AFTER_END_TIME(BAD_REQUEST, "RESERVATION_400_12", "시작 시간이 종료 시간보다 뒤입니다."),

	@ExplainError("예약 날짜가 yyyy-MM-dd 형식이 아닌 경우 발생하는 오류입니다.")
	INVALID_RESERVATION_DATE(BAD_REQUEST, "RESERVATION_400_13", "예약 날짜는 yyyy-MM-dd 형식이어야 합니다."),

	@ExplainError("이미 예약 대기 상태 혹은 승인된 강의실인 경우 발생하는 오류입니다.")
	DUPLICATE_RESERVATION(BAD_REQUEST, "RESERVATION_400_14", "이미 예약 대기 상태 혹은 승인된 강의실입니다.");

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