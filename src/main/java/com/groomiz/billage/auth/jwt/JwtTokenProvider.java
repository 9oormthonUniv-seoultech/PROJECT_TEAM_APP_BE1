package com.groomiz.billage.auth.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

	private final JwtUtil jwtUtil;

	public JwtTokenProvider(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	public String createAccessToken(String studentNumber, String role) {
		return jwtUtil.createJwt("AccessToken", studentNumber, role, 600000L);
	}

	public String createRefreshToken(String studentNumber, String role) {
		return jwtUtil.createJwt("RefreshToken", studentNumber, role, 86400000L);
	}
}
