package com.groomiz.billage.auth.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnivcertService {

	private final RestTemplate restTemplate;

	@Value("${univcert.api-key}")
	private String apiKey;

	public boolean sendEmailCertification(String email) {
		String url = "https://univcert.com/api/v1/certify";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("key", apiKey);
		requestBody.put("email", email);

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				Map<String, Object> result = new ObjectMapper().readValue(response.getBody(), Map.class);
				return Boolean.TRUE.equals(result.get("success"));
			} else {
				System.out.println("예상치 못한 응답: " + response.getBody());
				return false;
			}
		} catch (HttpClientErrorException e) {
			System.out.println("HTTP 요청 오류: " + e.getMessage());
			return false;
		} catch (Exception e) {
			System.out.println("오류 발생: " + e.getMessage());
			return false;
		}
	}

	public Map<String, Object> verifyEmailCertification(String email, int code) {
		String url = "https://univcert.com/api/v1/certifycode";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, Object> body = new HashMap<>();
		body.put("key", apiKey);
		body.put("email", email);
		body.put("code", code);

		// 요청 보내기
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

		try {
			ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
			Map<String, Object> responseBody = response.getBody();

			if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
				return responseBody;  // 인증 성공 시 응답 데이터 반환
			} else {
				Map<String, Object> failureResponse = new HashMap<>();
				failureResponse.put("success", false);
				failureResponse.put("message", responseBody != null ? responseBody.get("message") : "인증 실패");
				return failureResponse;
			}
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "서버 오류 발생: " + e.getMessage());
			return errorResponse;
		}
	}
}