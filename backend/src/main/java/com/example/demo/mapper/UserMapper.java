package com.example.demo.mapper;

import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserRequestDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between User entity and DTOs
 */
@Component
public class UserMapper {

	/**
	 * Convert User entity to UserDTO
	 * 
	 * @param user - User entity
	 * @return UserDTO
	 */
	public UserDTO toDTO(User user) {
		if (user == null) {
			return null;
		}

		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setFullName(user.getFullName());
		dto.setStatus(user.getStatus());
		dto.setRole(user.getRole());
		dto.setCreatedAt(user.getCreatedAt());
		return dto;
	}

	/**
	 * Convert UserRequestDTO to User entity
	 * Note: password should be hashed before setting
	 * 
	 * @param requestDTO - UserRequestDTO
	 * @return User entity
	 */
	public User toEntity(UserRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}

		User user = new User();
		user.setEmail(requestDTO.getEmail());
		user.setPasswordHash(requestDTO.getPassword());
		user.setFullName(requestDTO.getFullName());
		return user;
	}

	/**
	 * Update User entity from UserUpdateDTO
	 * 
	 * @param user     - User entity to update
	 * @param updateDTO - UserUpdateDTO with new values
	 */
	public void updateEntityFromDTO(User user, UserUpdateDTO updateDTO) {
		if (user == null || updateDTO == null) {
			return;
		}

		if (updateDTO.getEmail() != null) {
			user.setEmail(updateDTO.getEmail());
		}

		if (updateDTO.getFullName() != null) {
			user.setFullName(updateDTO.getFullName());
		}
	}
}
