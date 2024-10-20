package com.groomiz.billage.reservation.document;

import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;

@ExceptionDoc
public class ReservationExceptionDocs implements SwaggerExampleExceptions {

	@ExplainError
	public GlobalCodeException 토큰_만료 = new AuthException(AuthErrorCode.TOKEN_EXPIRED);

	@ExplainError
	public GlobalCodeException 토큰_없음 = new AuthException(AuthErrorCode.TOKEN_NOT_FOUND);

	@ExplainError
	public GlobalCodeException 토큰_유효하지_않음 = new AuthException(AuthErrorCode.INVALID_TOKEN);

	@ExplainError("전화번호 형식이 맞지 않는 경우 발생하는 오류입니다.")
	public GlobalCodeException 전화번호_형식_오류 = new MemberException(MemberErrorCode.INVALID_PHONE_NUMBER);

	@ExplainError("예약 날짜가 현재 날짜보다 이전인 경우 발생하는 오류입니다.")
	public GlobalCodeException 예약_날짜_오류 = new ReservationException(ReservationErrorCode.PAST_DATE_RESERVATION);

	@ExplainError("예약 날짜가 한달 이후인 경우 발생하는 오류입니다.")
	public GlobalCodeException 예약_날짜_한달_이후 = new ReservationException(ReservationErrorCode.FUTURE_DATE_LIMIT_EXCEEDED);

	@ExplainError("인원이 음수인 경우 발생하는 오류입니다.")
	public GlobalCodeException 음수_인원 = new ReservationException(ReservationErrorCode.NEGATIVE_PARTICIPANTS);

	@ExplainError("예약 시간이 24시 형식이 아닌 경우 발생하는 오류입니다.")
	public GlobalCodeException 예약_시간_오류 = new ReservationException(ReservationErrorCode.INVALID_RESERVATION_TIME);

	@ExplainError("예약 인원이 최대 인원을 초과한 경우 발생하는 오류입니다.")
	public GlobalCodeException 최대_인원_초과 = new ReservationException(ReservationErrorCode.EXCEED_MAX_PARTICIPANTS);

	@ExplainError("예약 목적이 잘못된 형식인 경우에 발생하는 오류입니다.")
	public GlobalCodeException 잘못된_목적 = new ReservationException(ReservationErrorCode.INVALID_RESERVATION_PURPOSE);

	@ExplainError("예약 목적이 기타인데, 기타에 목적이 없는 경우 발생하는 오류입니다.")
	public GlobalCodeException 기타_목적_오류 = new ReservationException(ReservationErrorCode.NO_PURPOSE_IN_ETC);

	@ExplainError("예약 시작 시간이 종료 시간보다 늦은 경우 발생하는 오류입니다.")
	public GlobalCodeException 시작_종료_시간_오류 = new ReservationException(ReservationErrorCode.START_TIME_AFTER_END_TIME);

	@ExplainError("이미 예약 중인 강의실에 대해 중복 예약을 시도한 경우 발생하는 오류입니다.")
	public GlobalCodeException 중복_예약 = new ReservationException(ReservationErrorCode.DUPLICATE_RESERVATION);
}