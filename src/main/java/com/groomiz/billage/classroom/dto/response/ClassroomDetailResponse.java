package com.groomiz.billage.classroom.dto.response;

import java.util.List;

import com.groomiz.billage.classroom.dto.ReservationTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "강의실 상세 조회 응답 DTO")
public class ClassroomDetailResponse {

	@Schema(description = "건물 ID", example = "40")
	private Long buildingId;
	@Schema(description = "건물 이름", example = "어의관")
	private String buildingName;
	@Schema(description = "강의실 ID", example = "1")
	private Long classroomId;
	@Schema(description = "강의실 이름", example = "실험실")
	private String classroomName;
	@Schema(description = "강의실 번호", example = "101")
	private String classroomNumber;
	@Schema(description = "수용 인원", example = "30")
	private Integer capacity;
	@Schema(description = "강의실 설명", example = "빔 프로젝터, 컴퓨터, 사물함")
	private String description;
	@Schema(description = "강의실 이미지", example = "https://groomiz.com/classroom/1.jpg")
	private List<String> classroomImage;
	@Schema(description = "예약된 시간", example = "[{\"startTime\":\"09:00\",\"endTime\":\"10:00\"}]")
	private List<ReservationTime> reservationTimes;
}
