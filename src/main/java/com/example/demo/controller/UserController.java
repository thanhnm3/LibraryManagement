package com.example.demo.controller;

import com.example.demo.dto.user.ChangePasswordDTO;
import com.example.demo.dto.user.UpdateUserRoleDTO;
import com.example.demo.dto.user.UpdateUserStatusDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserRequestDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.enums.UserRole;
import com.example.demo.enums.UserStatus;
import com.example.demo.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * UC-USER-001: Đăng ký tài khoản mới
	 */
	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRequestDTO requestDTO) {
		UserDTO createdUser = userService.registerUser(requestDTO);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	/**
	 * UC-USER-002: Lấy danh sách người dùng
	 */
	@GetMapping
	public ResponseEntity<Page<UserDTO>> getAllUsers(
		@PageableDefault(size = 20) Pageable pageable,
		@RequestParam(required = false) UserStatus status,
		@RequestParam(required = false) UserRole role
	) {
		Page<UserDTO> users = userService.getAllUsers(pageable, status, role);
		return ResponseEntity.ok(users);
	}

	/**
	 * UC-USER-003: Lấy thông tin người dùng
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
		UserDTO user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	/**
	 * UC-USER-004: Cập nhật thông tin người dùng
	 */
	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> updateUser(
		@PathVariable Long id,
		@Valid @RequestBody UserUpdateDTO updateDTO
	) {
		UserDTO updatedUser = userService.updateUser(id, updateDTO);
		return ResponseEntity.ok(updatedUser);
	}

	/**
	 * UC-USER-005: Thay đổi mật khẩu
	 */
	@PutMapping("/{id}/password")
	public ResponseEntity<Void> changePassword(
		@PathVariable Long id,
		@Valid @RequestBody ChangePasswordDTO changePasswordDTO
	) {
		userService.changePassword(id, changePasswordDTO);
		return ResponseEntity.noContent().build();
	}

	/**
	 * UC-USER-006: Cập nhật trạng thái người dùng
	 */
	@PutMapping("/{id}/status")
	public ResponseEntity<UserDTO> updateUserStatus(
		@PathVariable Long id,
		@Valid @RequestBody UpdateUserStatusDTO updateStatusDTO
	) {
		UserDTO updatedUser = userService.updateUserStatus(id, updateStatusDTO);
		return ResponseEntity.ok(updatedUser);
	}

	/**
	 * UC-USER-007: Cập nhật role người dùng
	 */
	@PutMapping("/{id}/role")
	public ResponseEntity<UserDTO> updateUserRole(
		@PathVariable Long id,
		@Valid @RequestBody UpdateUserRoleDTO updateRoleDTO
	) {
		UserDTO updatedUser = userService.updateUserRole(id, updateRoleDTO);
		return ResponseEntity.ok(updatedUser);
	}
}
