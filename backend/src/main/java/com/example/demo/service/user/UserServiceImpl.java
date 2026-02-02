package com.example.demo.service.user;

import com.example.demo.dto.user.ChangePasswordDTO;
import com.example.demo.dto.user.UpdateUserRoleDTO;
import com.example.demo.dto.user.UpdateUserStatusDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserRequestDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.enums.UserStatus;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for User operations (UC-USER-001 ~ UC-USER-007)
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(
			UserRepository userRepository,
			UserMapper userMapper,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDTO registerUser(UserRequestDTO requestDTO) {
		if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
			throw new DuplicateResourceException("Email already exists: " + requestDTO.getEmail());
		}

		User user = userMapper.toEntity(requestDTO);
		user.setPasswordHash(passwordEncoder.encode(requestDTO.getPassword()));
		User savedUser = userRepository.save(user);
		return userMapper.toDTO(savedUser);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserDTO> getAllUsers(Pageable pageable, UserStatus status, UserRole role) {
		return userRepository.findAllWithFilters(status, role, pageable).map(userMapper::toDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
		return userMapper.toDTO(user);
	}

	@Override
	public UserDTO updateUser(Long userId, UserUpdateDTO updateDTO) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
		userMapper.updateEntityFromDTO(user, updateDTO);
		User updatedUser = userRepository.save(user);
		return userMapper.toDTO(updatedUser);
	}

	@Override
	public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

		if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPasswordHash())) {
			throw new BusinessException("Old password is incorrect");
		}

		user.setPasswordHash(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
		userRepository.save(user);
	}

	@Override
	public UserDTO updateUserStatus(Long userId, UpdateUserStatusDTO updateStatusDTO) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
		user.setStatus(updateStatusDTO.getStatus());
		User updatedUser = userRepository.save(user);
		return userMapper.toDTO(updatedUser);
	}

	@Override
	public UserDTO updateUserRole(Long userId, UpdateUserRoleDTO updateRoleDTO) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
		user.setRole(updateRoleDTO.getRole());
		User updatedUser = userRepository.save(user);
		return userMapper.toDTO(updatedUser);
	}

	@Override
	@Transactional(readOnly = true)
	public User authenticate(String email, String password) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new BusinessException("Invalid email or password"));
		if (user.getStatus() != UserStatus.ACTIVE) {
			throw new BusinessException("Account is not active");
		}
		if (!passwordEncoder.matches(password, user.getPasswordHash())) {
			throw new BusinessException("Invalid email or password");
		}
		return user;
	}
}
