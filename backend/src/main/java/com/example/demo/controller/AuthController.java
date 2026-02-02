package com.example.demo.controller;

import com.example.demo.dto.auth.LoginRequestDTO;
import com.example.demo.dto.auth.LoginResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;

	public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
		this.userService = userService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	/**
	 * Login: validate credentials, return JWT and user info. No token required.
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
		User user = userService.authenticate(request.getEmail(), request.getPassword());
		String accessToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());
		LoginResponseDTO.AuthUserDTO userDto = new LoginResponseDTO.AuthUserDTO(
				user.getId(),
				user.getEmail(),
				user.getFullName(),
				user.getRole()
		);
		return ResponseEntity.ok(new LoginResponseDTO(accessToken, userDto));
	}

	/**
	 * Return current user from token. Used by frontend to restore user after reload.
	 */
	@GetMapping("/me")
	public ResponseEntity<LoginResponseDTO.AuthUserDTO> me(Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		var userDto = userService.getUserById(principal.getId());
		LoginResponseDTO.AuthUserDTO me = new LoginResponseDTO.AuthUserDTO(
				userDto.getId(),
				userDto.getEmail(),
				userDto.getFullName(),
				userDto.getRole()
		);
		return ResponseEntity.ok(me);
	}
}
