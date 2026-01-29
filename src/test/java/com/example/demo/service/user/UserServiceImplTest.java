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
import com.example.demo.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for UserService implementation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  private UserRequestDTO userRequestDTO;
  private User user;
  private UserDTO userDTO;
  private UserUpdateDTO userUpdateDTO;
  private ChangePasswordDTO changePasswordDTO;
  private UpdateUserStatusDTO updateUserStatusDTO;
  private UpdateUserRoleDTO updateUserRoleDTO;

  @BeforeEach
  void setUp() {
    userRequestDTO = TestDataBuilder.createUserRequestDTO();
    user = TestDataBuilder.createUser();
    userDTO = TestDataBuilder.createUserDTO();
    userUpdateDTO = new UserUpdateDTO();
    userUpdateDTO.setEmail("updated@example.com");
    userUpdateDTO.setFullName("Updated User");
    changePasswordDTO = new ChangePasswordDTO();
    changePasswordDTO.setOldPassword("oldPassword");
    changePasswordDTO.setNewPassword("newPassword123");
    updateUserStatusDTO = new UpdateUserStatusDTO();
    updateUserStatusDTO.setStatus(UserStatus.BANNED);
    updateUserRoleDTO = new UpdateUserRoleDTO();
    updateUserRoleDTO.setRole(UserRole.ADMIN);
  }

  @Test
  @DisplayName("UC-USER-001: shouldRegisterUser_WhenValidRequest")
  void shouldRegisterUser_WhenValidRequest() {
    // Arrange
    when(userRepository.findByEmail(userRequestDTO.getEmail())).thenReturn(Optional.empty());
    when(userMapper.toEntity(userRequestDTO)).thenReturn(user);
    when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("hashedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(userMapper.toDTO(user)).thenReturn(userDTO);

    // Act
    UserDTO result = userService.registerUser(userRequestDTO);

    // Assert
    assertNotNull(result);
    assertEquals(userDTO.getId(), result.getId());
    verify(userRepository, times(1)).findByEmail(userRequestDTO.getEmail());
    verify(passwordEncoder, times(1)).encode(userRequestDTO.getPassword());
    verify(userRepository, times(1)).save(any(User.class));
    verify(userMapper, times(1)).toDTO(user);
  }

  @Test
  @DisplayName("UC-USER-001: shouldThrowException_WhenEmailExists")
  void shouldThrowException_WhenEmailExists() {
    // Arrange
    when(userRepository.findByEmail(userRequestDTO.getEmail())).thenReturn(Optional.of(user));

    // Act & Assert
    assertThrows(DuplicateResourceException.class, () -> userService.registerUser(userRequestDTO));
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("UC-USER-002: shouldGetAllUsers_WithFilters")
  void shouldGetAllUsers_WithFilters() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<User> users = List.of(user);
    Page<User> userPage = new PageImpl<>(users, pageable, 1);

    when(userRepository.findAllWithFilters(UserStatus.ACTIVE, null, pageable)).thenReturn(userPage);
    when(userMapper.toDTO(user)).thenReturn(userDTO);

    // Act
    Page<UserDTO> result = userService.getAllUsers(pageable, UserStatus.ACTIVE, null);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(userRepository, times(1)).findAllWithFilters(UserStatus.ACTIVE, null, pageable);
    verify(userMapper, times(1)).toDTO(user);
  }

  @Test
  @DisplayName("UC-USER-003: shouldGetUserById_WhenExists")
  void shouldGetUserById_WhenExists() {
    // Arrange
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userMapper.toDTO(user)).thenReturn(userDTO);

    // Act
    UserDTO result = userService.getUserById(userId);

    // Assert
    assertNotNull(result);
    assertEquals(userDTO.getId(), result.getId());
    verify(userRepository, times(1)).findById(userId);
    verify(userMapper, times(1)).toDTO(user);
  }

  @Test
  @DisplayName("UC-USER-003: shouldThrowException_WhenUserNotFound")
  void shouldThrowException_WhenUserNotFound() {
    // Arrange
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    verify(userMapper, never()).toDTO(any());
  }

  @Test
  @DisplayName("UC-USER-004: shouldUpdateUser_WhenValidRequest")
  void shouldUpdateUser_WhenValidRequest() {
    // Arrange
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toDTO(user)).thenReturn(userDTO);

    // Act
    UserDTO result = userService.updateUser(userId, userUpdateDTO);

    // Assert
    assertNotNull(result);
    verify(userRepository, times(1)).findById(userId);
    verify(userMapper, times(1)).updateEntityFromDTO(user, userUpdateDTO);
    verify(userRepository, times(1)).save(user);
    verify(userMapper, times(1)).toDTO(user);
  }

  @Test
  @DisplayName("UC-USER-005: shouldChangePassword_WhenOldPasswordCorrect")
  void shouldChangePassword_WhenOldPasswordCorrect() {
    // Arrange
    Long userId = 1L;
    user.setPasswordHash("hashedOldPassword");
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPasswordHash())).thenReturn(true);
    when(passwordEncoder.encode(changePasswordDTO.getNewPassword())).thenReturn("hashedNewPassword");
    when(userRepository.save(user)).thenReturn(user);

    // Act
    userService.changePassword(userId, changePasswordDTO);

    // Assert
    verify(userRepository, times(1)).findById(userId);
    verify(passwordEncoder, times(1)).matches(changePasswordDTO.getOldPassword(), user.getPasswordHash());
    verify(passwordEncoder, times(1)).encode(changePasswordDTO.getNewPassword());
    verify(userRepository, times(1)).save(user);
  }

  @Test
  @DisplayName("UC-USER-005: shouldThrowException_WhenOldPasswordIncorrect")
  void shouldThrowException_WhenOldPasswordIncorrect() {
    // Arrange
    Long userId = 1L;
    user.setPasswordHash("hashedOldPassword");
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPasswordHash())).thenReturn(false);

    // Act & Assert
    assertThrows(BusinessException.class, () -> userService.changePassword(userId, changePasswordDTO));
    verify(passwordEncoder, never()).encode(anyString());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("UC-USER-006: shouldUpdateUserStatus_WhenValid")
  void shouldUpdateUserStatus_WhenValid() {
    // Arrange
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toDTO(user)).thenReturn(userDTO);

    // Act
    UserDTO result = userService.updateUserStatus(userId, updateUserStatusDTO);

    // Assert
    assertNotNull(result);
    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(user);
    verify(userMapper, times(1)).toDTO(user);
  }

  @Test
  @DisplayName("UC-USER-007: shouldUpdateUserRole_WhenValid")
  void shouldUpdateUserRole_WhenValid() {
    // Arrange
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toDTO(user)).thenReturn(userDTO);

    // Act
    UserDTO result = userService.updateUserRole(userId, updateUserRoleDTO);

    // Assert
    assertNotNull(result);
    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(user);
    verify(userMapper, times(1)).toDTO(user);
  }
}
