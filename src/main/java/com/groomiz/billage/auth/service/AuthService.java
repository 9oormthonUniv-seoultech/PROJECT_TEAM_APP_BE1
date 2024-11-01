package com.groomiz.billage.auth.service;

import java.time.Duration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.groomiz.billage.auth.dto.LoginRequest;
import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.auth.jwt.JwtTokenProvider;
import com.groomiz.billage.auth.jwt.JwtUtil;

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
		String username = jwtUtil.getStudentNumber(refreshToken);
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
}
