package com.groomiz.billage.auth.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groomiz.billage.auth.dto.LoginRequest;
import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.auth.jwt.JwtTokenProvider;
import com.groomiz.billage.auth.jwt.JwtUtil;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.repository.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisService redisService;
	private final JwtUtil jwtUtil;

	private final MemberRepository memberRepository;

	public void login(LoginRequest loginRequest, HttpServletResponse response) throws AuthenticationException {
		// 로그인 인증 처리
		Authentication authentication = authenticate(loginRequest);

		String username = authentication.getName();
		String role = authentication.getAuthorities().iterator().next().getAuthority();

		// AccessToken과 RefreshToken 생성
		String accessToken = jwtTokenProvider.createAccessToken(username, role);
		String refreshToken = jwtTokenProvider.createRefreshToken(username, role);

		// Redis에 RefreshToken 저장
		redisService.setValues(username, refreshToken, Duration.ofMillis(86400000L));  // 1일 유효

		// AccessToken과 RefreshToken을 헤더에 추가
		response.setHeader("Authorization", "Bearer " + accessToken);
		response.setHeader("RefreshToken", "Bearer " + refreshToken);
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		// 헤더에서 RefreshToken 가져오기
		String refreshToken = request.getHeader("RefreshToken");
		if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
			throw new AuthException(AuthErrorCode.TOKEN_NOT_FOUND);
		}

		// Bearer 접두사 제거
		refreshToken = refreshToken.substring(7);

		// RefreshToken 만료 확인
		if (jwtUtil.isExpired(refreshToken)) {
			throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
		}

		// RefreshToken의 유효성 확인
		String category = jwtUtil.getCategory(refreshToken);
		String username = jwtUtil.getUsername(refreshToken);
		if (!"RefreshToken".equals(category) || !redisService.checkExistsValue(refreshToken)) {
			throw new AuthException(AuthErrorCode.INVALID_TOKEN);
		}

		// Redis에서 RefreshToken 삭제
		redisService.deleteValues(username);

		// 응답에서 RefreshToken 헤더를 제거
		response.setHeader("RefreshToken", "");
	}

	private Authentication authenticate(LoginRequest loginRequest) throws AuthenticationException {
		UsernamePasswordAuthenticationToken authToken =
			new UsernamePasswordAuthenticationToken(loginRequest.getStudentNumber(), loginRequest.getPassword());
		return authenticationManager.authenticate(authToken);
	}

	public void checkStudentNumber(String studentNumber) {
		if (String.valueOf(studentNumber).length() != 8) {
			throw new MemberException(MemberErrorCode.INVALID_STUDENT_ID);
		}

		boolean exists = memberRepository.existsByStudentNumber(studentNumber);
		if (exists) {
			throw new MemberException(MemberErrorCode.STUDENT_ID_ALREADY_REGISTERED);
		}

	}

}
