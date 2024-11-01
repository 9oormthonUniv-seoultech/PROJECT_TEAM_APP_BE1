package com.groomiz.billage.auth.service;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.groomiz.billage.auth.jwt.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final JwtUtil jwtUtil;
	private final RedisService redisService;

	public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

		// 헤더에서 Bearer 형식의 RefreshToken 가져오기
		String refreshToken = extractToken(request.getHeader("RefreshToken"));
		if (refreshToken == null) {
			return new ResponseEntity<>("Refresh token is null or not in the correct format", HttpStatus.BAD_REQUEST);
		}

		// RefreshToken이 만료되었는지 확인
		try {
			jwtUtil.isExpired(refreshToken);
		} catch (ExpiredJwtException e) {
			return new ResponseEntity<>("Refresh token expired", HttpStatus.BAD_REQUEST);
		}

		String studentNumber = jwtUtil.getStudentNumber(refreshToken);
		String storedToken = redisService.getValues(studentNumber);

		// Redis에 저장된 토큰과 비교하여 유효성 검증
		if (!refreshToken.equals(storedToken)) {
			return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
		}

		// 새로운 AccessToken 및 RefreshToken 생성
		String role = jwtUtil.getRole(refreshToken);
		String newAccessToken = jwtUtil.createJwt("AccessToken", studentNumber, role, 600000L); // 10분
		String newRefreshToken = jwtUtil.createJwt("RefreshToken", studentNumber, role, 86400000L); // 1일

		// 기존 토큰 삭제 및 새 토큰 저장
		redisService.deleteValues(studentNumber);
		redisService.setValues(studentNumber, newRefreshToken, Duration.ofDays(1));

		// 새로운 토큰을 헤더에 Bearer 형식으로 추가
		response.setHeader("Authorization", "Bearer " + newAccessToken);
		response.setHeader("RefreshToken", "Bearer " + newRefreshToken);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	// Bearer 토큰 형식에서 토큰만 추출하는 메서드
	private String extractToken(String header) {
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}
}
