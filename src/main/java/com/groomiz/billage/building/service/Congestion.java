package com.groomiz.billage.building.service;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.groomiz.billage.building.exception.BuildingErrorCode;
import com.groomiz.billage.building.exception.BuildingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Congestion {

	LOW("여유"),
	AVG("보통"),
	HIGH("혼잡");

	private final String description;

	private static final Map<String, Congestion> DESCRIPTION_TO_ENUM_MAP = new HashMap<>();

	static {
		for (Congestion congestion : Congestion.values()) {
			DESCRIPTION_TO_ENUM_MAP.put(congestion.getDescription(), congestion);
		}
	}

	public static Congestion fromRatio(double ratio) {
		if (ratio < 0.3) {
			return LOW;
		} else if (ratio < 0.7) {
			return AVG;
		} else {
			return HIGH;
		}
	}

	@JsonValue
	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static Congestion fromDescription(String description) {
		Congestion congestion = DESCRIPTION_TO_ENUM_MAP.get(description);

		if (congestion == null) {
			throw new BuildingException(BuildingErrorCode.INVALID_CONGESTION_LEVEL);
		}

		return congestion;
	}
}
