package com.groomiz.billage.building.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "건물 목록 조회 응답 DTO")
public class BuildingListResponse {

	@Schema(description = "건물 ID", example = "1")
	private Long buildingId;
	@Schema(description = "건물 이름", example = "미래관")
	private String buildingName;
	@Schema(description = "건물 번호", example = "39")
	private String buildingNumber;
	@Schema(description = "층 리스트", example = "[1, 2, 3]")
	private List<Long> floors;


}
