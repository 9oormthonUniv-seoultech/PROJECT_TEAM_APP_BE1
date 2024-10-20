package com.groomiz.billage.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private final SecretKey secretKey;

	public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			SignatureAlgorithm.HS256.getJcaName());
	}

	public String getStudentNumber(String token) {
		try {
			return Jwts.parser()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("studentNumber", String.class);
		} catch (ExpiredJwtException e) {
			// 토큰이 만료된 경우 예외 처리
			System.out.println("JWT expired.");
			return null;
		} catch (JwtException e) {
			// 기타 JWT 처리 오류
			System.out.println("JWT processing failed.");
			return null;
		}
	}

	public String getRole(String token) {
		try {
			return Jwts.parser()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("role", String.class);
		} catch (ExpiredJwtException e) {
			System.out.println("JWT expired.");
			return null;
		} catch (JwtException e) {
			System.out.println("JWT processing failed.");
			return null;
		}
	}

	public String getCategory(String token) {
		try {
			return Jwts.parser()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("category", String.class);
		} catch (ExpiredJwtException e) {
			System.out.println("JWT expired.");
			return null;
		} catch (JwtException e) {
			System.out.println("JWT processing failed.");
			return null;
		}
	}

	public Boolean isExpired(String token) {
		try {
			Date expirationDate = Jwts.parser()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getExpiration();

			return expirationDate.before(new Date());
		} catch (ExpiredJwtException e) {
			// 토큰이 만료된 경우 true 반환
			return true;
		} catch (JwtException e) {
			// 기타 오류가 발생한 경우에도 만료된 것으로 간주
			return true;
		}
	}

	public String createJwt(String category, String studentNumber, String role, Long expiredMs) {
		return Jwts.builder()
			.claim("category", category)
			.claim("studentNumber", studentNumber)
			.claim("role", role)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiredMs))
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

}
