package com.example.demo.security;

import com.example.demo.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Generates and validates JWT access tokens. Uses HS256 with configurable secret and expiration.
 */
@Component
public class JwtTokenProvider {

	private static final String CLAIM_USER_ID = "userId";
	private static final String CLAIM_EMAIL = "email";
	private static final String CLAIM_ROLE = "role";

	private final SecretKey secretKey;
	private final long expirationMs;

	public JwtTokenProvider(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.expiration-ms}") long expirationMs) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMs = expirationMs;
	}

	public String generateToken(Long userId, String email, UserRole role) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationMs);
		return Jwts.builder()
				.subject(email)
				.claim(CLAIM_USER_ID, userId)
				.claim(CLAIM_EMAIL, email)
				.claim(CLAIM_ROLE, role.name())
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey)
				.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getUserIdFromToken(String token) {
		Claims payload = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
		Object userId = payload.get(CLAIM_USER_ID);
		if (userId instanceof Number) {
			return ((Number) userId).longValue();
		}
		return null;
	}

	public String getEmailFromToken(String token) {
		Claims payload = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
		return payload.get(CLAIM_EMAIL, String.class);
	}

	public UserRole getRoleFromToken(String token) {
		Claims payload = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
		String roleStr = payload.get(CLAIM_ROLE, String.class);
		return roleStr != null ? UserRole.valueOf(roleStr) : null;
	}
}
