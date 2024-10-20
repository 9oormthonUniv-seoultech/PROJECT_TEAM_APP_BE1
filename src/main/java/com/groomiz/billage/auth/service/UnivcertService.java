package com.groomiz.billage.auth.service;

import java.io.IOException;
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
import com.univcert.api.UnivCert;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnivcertService {

	@Value("${univcert.api-key}")
	private String apiKey;

	public Map<?, ?> certifyEmail(String email, String univName, boolean univCheck) {
		try {
			Map<String, Object> response = UnivCert.certify(apiKey, email, univName, univCheck);
			boolean success = (Boolean) response.get("success");

			if (success) {
				response.put("message", "인증번호 발송 성공");
			} else {
				response.put("message", response.getOrDefault("message", "인증번호 발송 실패"));
			}
			return response;

		} catch (IOException e) {
			return Map.of("success", false, "message", "오류가 발생했습니다.");
		}
	}

	public Map<?, ?> verifyEmail(String email, String univName,  int code) {
		try {
			// UnivCert API 호출
			Map<String, Object> response = UnivCert.certifyCode(apiKey, email, univName, code);
			boolean success = (Boolean) response.get("success");

			// 성공 여부에 따라 메시지 설정
			if (success) {
				response.put("message", "인증번호가 일치합니다l");
			} else {
				response.put("message", response.getOrDefault("message", "인증번호가 일치하지 않습니다."));
			}

			return response; // 응답을 그대로 반환

		} catch (IOException e) {
			// IOException 발생 시 응답 설정
			return Map.of("success", false, "message", "오류가 발생했습니다.");
		}
	}
}