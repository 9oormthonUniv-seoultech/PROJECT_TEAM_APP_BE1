package com.groomiz.billage.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.groomiz.billage.member.dto.response.CollegeListResponse;
import com.groomiz.billage.member.entity.College;

@Service
public class CollegeService {

	public List<CollegeListResponse> findAllCollegesMajors() {
		return List.of(College.values())
			.stream()
			.map(college -> new CollegeListResponse(college))
			.collect(Collectors.toList());
	}
}
