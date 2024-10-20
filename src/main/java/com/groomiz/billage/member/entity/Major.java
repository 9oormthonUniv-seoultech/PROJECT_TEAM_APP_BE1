package com.groomiz.billage.member.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Major {
	MSD("기계시스템디자인공학과", College.ENGINEER),
	MAE("기계자동차공학과", College.ENGINEER),
	SAFETY("안전공학과", College.ENGINEER),
	MSE("신소재공학과", College.ENGINEER),
	CIVIL("건설시스템공학과", College.ENGINEER),
	ARCHITECTURE("건축학부(건축공학전공)", College.ENGINEER),
	ARCHIDESIGN("건축학부(건축학전공)", College.ENGINEER),
	EBSE("건축기계설비공학과", College.ENGINEER),

	ECE("전기정보공학과", College.ICE),
	EE("전자공학과", College.ICE),
	CS("컴퓨터공학과", College.ICE),
	ICTE("스마트ICT융합공학과", College.ICE),

	CHE("화공생명공학과", College.NATURE),
	ENVIRO("환경공학과", College.NATURE),
	FOOD("식품생명공학과", College.NATURE),
	FCHEM("정밀화학과", College.NATURE),
	SPORTS("스포츠과학과", College.NATURE),
	OPTOMETRY("안경광학과", College.NATURE),

	ID("디자인학과(산업디자인전공)", College.AND),
	VD("디자인학과(시각디자인전공)", College.AND),
	CERAMICS("도예학과", College.AND),
	METALARTDESIGN("금속공예디자인학과", College.AND),
	FINEART("조형예술학과", College.AND),

	PA("행정학과", College.HUMAN),
	ENG("영어영문학과", College.HUMAN),
	WRITING_CREATIVE("문예창작학과", College.HUMAN),

	IISE("산업공학과(산업정보시스템전공)", College.SGC),
	ITM("산업공학과(ITM전공)", College.SGC),
	MSDE("MSDE학과", College.SGC),
	BIZ("경영학과(경영학전공)", College.SGC),
	GTM("경영학과(글로벌테크노경영전공)", College.SGC),

	CME("융합기계공학과", College.M_DISCIPLINARY),
	CIVIL_ENV("건설환경융합공학과", College.M_DISCIPLINARY),
	WELLNESS("헬스피트니스학과", College.M_DISCIPLINARY),
	CSB("문화예술학과", College.M_DISCIPLINARY),
	ENG_CULTURE("영어과", College.M_DISCIPLINARY),
	V_BIZ("벤처경영학과", College.M_DISCIPLINARY),
	COMM("정보통신융합공학과", College.M_DISCIPLINARY),

	AAI("인공지능응용학과", College.CCCS),
	DSE("지능형반도체공학과", College.CCCS),
	FUTURE_ENERGY("미래에너지융합학과", College.CCCS);

	private final String name;
	private final College college;

	private static final Map<String, Major> NAME_TO_ENUM_MAP = new HashMap<>();

	static {
		for (Major major : Major.values()) {
			NAME_TO_ENUM_MAP.put(major.name, major);
		}
	}

	@JsonValue
	public String getName() {
		return name;
	}

	@JsonCreator
	public static Major fromName(String name) {
		Major major = NAME_TO_ENUM_MAP.get(name);

		if (major == null) {
			throw new MemberException(MemberErrorCode.INVALID_MAJOR_ENUM);
		}

		return major;
	}

	// 단과대에 해당하는 학과 리스트 반환하는 함수
	public static List<Major> getMajorsByCollege(College college) {
		return Arrays.stream(Major.values())
			.filter(major -> major.getCollege() == college)
			.collect(Collectors.toList());
	}
}