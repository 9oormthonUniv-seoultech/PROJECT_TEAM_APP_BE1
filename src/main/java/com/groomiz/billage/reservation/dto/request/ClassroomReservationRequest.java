package com.groomiz.billage.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import com.groomiz.billage.reservation.entity.ReservationPurpose;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "강의실 예약 요청 DTO")
public class ClassroomReservationRequest {

	@Schema(description = "강의실 ID", example = "1")
	@NotNull
	private Long classroomId;
	@Schema(description = "예약자 추가 전화번호", example = "010-1234-5678")
	private String phoneNumber;
	@Schema(description = "예약 날짜", example = "2024-08-01")
	@NotNull
	private LocalDate applyDate;
	@Schema(description = "예약 시작 시간", example = "09:00")
	@NotNull
	@Pattern(regexp = "\\d{2}-\\d{2}", message = "시간 형식은 HH:mm이어야 합니다.")
	private LocalTime startTime;
	@Schema(description = "예약 종료 시간", example = "10:00")
	@Pattern(regexp = "\\d{2}-\\d{2}", message = "시간 형식은 HH:mm이어야 합니다.")
	@NotNull
	private LocalTime endTime;
	@Schema(description = "예약 인원", example = "30")
	@NotNull
	private Integer headcount;
	@Schema(description = "예약 사유", example = "단과대 행사")
	@NotNull
	private ReservationPurpose purpose;
	@Schema(description = "비고", example = "공대인의 밤 행사입니다.")
	private String contents;
}
