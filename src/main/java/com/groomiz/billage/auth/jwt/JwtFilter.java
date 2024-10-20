package com.groomiz.billage.auth.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.groomiz.billage.auth.config.SecurityProperties;
import com.groomiz.billage.auth.dto.CustomUserDetails;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.entity.Role;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final SecurityProperties securityProperties;
	private List<String> whiteList;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		whiteList = securityProperties.getWhitelist();

		// 헤더에서 Authorization에 담긴 토큰을 꺼냄
		String authorizationHeader = request.getHeader("Authorization");

		// 토큰이 없다면 다음 필터로 넘김
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// Bearer 접두사를 제거하고 순수 토큰 값만 추출
		String accessToken = authorizationHeader.substring(7);

		try {
			// 토큰 만료 여부 확인 및 만료된 경우 예외 처리
			if (checkAuthRequired(request)) {
				jwtUtil.isExpired(accessToken);

				// 토큰이 AccessToken인지 확인 (발급시 페이로드에 명시)
				String category = jwtUtil.getCategory(accessToken);

				if (category == null || !category.equals("AccessToken")) {
					sendErrorResponse(response, "Invalid access token", HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}

				// username, role 값을 획득
				String studentNumber = jwtUtil.getStudentNumber(accessToken);
				String role = jwtUtil.getRole(accessToken);

				if (studentNumber == null || role == null) {
					sendErrorResponse(response, "Invalid token claims", HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}

				// SecurityContext에 인증 정보 설정
				Member member = Member.builder()
					.studentNumber(studentNumber)
					.role(Role.valueOf(role))
					.build();

				CustomUserDetails customUserDetails = new CustomUserDetails(member);

				Authentication authToken = new UsernamePasswordAuthenticationToken(
					customUserDetails, null, customUserDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			// 다음 필터로 넘김
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			sendErrorResponse(response, "Access token expired", HttpServletResponse.SC_UNAUTHORIZED);
		} catch (JwtException e) {
			sendErrorResponse(response, "JWT processing failed", HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
		response.setStatus(status);
		PrintWriter writer = response.getWriter();
		writer.print(message);
		writer.flush();
	}

	private boolean checkAuthRequired(HttpServletRequest request) {
		if (whiteList == null || whiteList.isEmpty()) {
			return true; // 인증 필요
		}

		// 요청이 whiteList에 포함된 경우 인증을 요구하지 않음
		for (String path : whiteList) {
			RequestMatcher matcher = new AntPathRequestMatcher(path);
			if (matcher.matches(request)) {
				return false; // whiteList에 포함된 경우 인증 불필요
			}
		}

		// whiteList에 포함되지 않은 경우 인증 필요
		return true;
	}
}
