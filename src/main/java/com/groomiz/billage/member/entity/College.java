package com.groomiz.billage.member.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum College {
	ENGINEER("공과대학"),
	ICE("정보통신대학"),
	NATURE("에너지바이오대학"),
	AND("조형대학"),
	HUMAN("인문사회대학"),
	SGC("기술경영융합대학"),
	M_DISCIPLINARY("미래융합대학"),
	CCCS("창의융합대학"),
	LIBERAL("교양대학"),
	IC("국제대학");

	private final String name;

	private static final Map<String, College> NAME_TO_ENUM_MAP = new HashMap<>();

	static {
		for (College college : College.values()) {
			NAME_TO_ENUM_MAP.put(college.name, college);
		}
	}

	@JsonValue
	public String getName() {
		return name;
	}

	@JsonCreator
	public static College fromName(String name) {
		College college = NAME_TO_ENUM_MAP.get(name);

		if (college == null) {
			// TODO: 예외 처리
		}

		return college;
	}
}