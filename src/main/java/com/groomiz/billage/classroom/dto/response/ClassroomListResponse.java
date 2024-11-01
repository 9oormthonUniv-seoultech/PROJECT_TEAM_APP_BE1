package com.groomiz.billage.classroom.dto.response;

import java.util.List;

import com.groomiz.billage.classroom.dto.ReservationTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "강의실 목록 조회 응답 DTO")
public class ClassroomListResponse {

	@Schema(description = "강의실 ID", example = "1")
	private Long classroomId;
	@Schema(description = "강의실 이름", example = "실험실")
	private String classroomName;
	@Schema(description = "강의실 번호", example = "101")
	private String classroomNumber;
	@Schema(description = "수용 인원", example = "30")
	private Integer capacity;
	@Schema(description = "예약된 시간", example = "[{\"startTime\":\"09:00\",\"endTime\":\"10:00\"}]")
	private List<ReservationTime> reservationTimes;

}
