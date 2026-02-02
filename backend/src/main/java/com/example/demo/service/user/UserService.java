package com.example.demo.service.user;

import com.example.demo.dto.user.ChangePasswordDTO;
import com.example.demo.entity.User;
import com.example.demo.dto.user.UpdateUserRoleDTO;
import com.example.demo.dto.user.UpdateUserStatusDTO;
import com.example.demo.dto.user.UserDTO;
import com.example.demo.dto.user.UserRequestDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.enums.UserRole;
import com.example.demo.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for User operations
 */
public interface UserService {

	/**
	 * Đăng ký tài khoản mới
	 * 
	 * @param requestDTO - Thông tin người dùng
	 * @return UserDTO
	 */
	UserDTO registerUser(UserRequestDTO requestDTO);

	/**
	 * Lấy danh sách người dùng
	 * 
	 * @param pageable - Thông tin phân trang
	 * @param status   - Lọc theo trạng thái (optional)
	 * @param role     - Lọc theo role (optional)
	 * @return Page<UserDTO>
	 */
	Page<UserDTO> getAllUsers(Pageable pageable, UserStatus status, UserRole role);

	/**
	 * Lấy thông tin người dùng
	 * 
	 * @param userId - ID người dùng
	 * @return UserDTO
	 */
	UserDTO getUserById(Long userId);

	/**
	 * Cập nhật thông tin người dùng
	 * 
	 * @param userId   - ID người dùng
	 * @param updateDTO - Thông tin cập nhật
	 * @return UserDTO
	 */
	UserDTO updateUser(Long userId, UserUpdateDTO updateDTO);

	/**
	 * Thay đổi mật khẩu
	 * 
	 * @param userId           - ID người dùng
	 * @param changePasswordDTO - Thông tin mật khẩu
	 */
	void changePassword(Long userId, ChangePasswordDTO changePasswordDTO);

	/**
	 * Cập nhật trạng thái người dùng
	 * 
	 * @param userId         - ID người dùng
	 * @param updateStatusDTO - Trạng thái mới
	 * @return UserDTO
	 */
	UserDTO updateUserStatus(Long userId, UpdateUserStatusDTO updateStatusDTO);

	/**
	 * Cập nhật role người dùng
	 * 
	 * @param userId       - ID người dùng
	 * @param updateRoleDTO - Role mới
	 * @return UserDTO
	 */
	UserDTO updateUserRole(Long userId, UpdateUserRoleDTO updateRoleDTO);

	/**
	 * Xác thực đăng nhập: tìm user theo email, kiểm tra ACTIVE và mật khẩu.
	 *
	 * @param email    - Email người dùng
	 * @param password - Mật khẩu plain
	 * @return User nếu hợp lệ
	 * @throws com.example.demo.exception.BusinessException nếu email/password sai hoặc tài khoản không ACTIVE
	 */
	User authenticate(String email, String password);
}
