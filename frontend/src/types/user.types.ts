import type { UserRole, UserStatus } from './enums';

/**
 * User list/summary DTO from API.
 */
export type UserDTO = {
  id: number;
  email: string;
  fullName: string;
  status: UserStatus;
  role: UserRole;
  createdAt: string;
};

/**
 * Request body for creating a user.
 */
export type UserRequestDTO = {
  email: string;
  password: string;
  fullName: string;
};

/**
 * Request body for updating a user (all fields optional).
 */
export type UserUpdateDTO = {
  email?: string;
  fullName?: string;
};

/**
 * Request body for changing password.
 */
export type ChangePasswordDTO = {
  oldPassword: string;
  newPassword: string;
};

/**
 * Request body for updating user role.
 */
export type UpdateUserRoleDTO = {
  role: UserRole;
};

/**
 * Request body for updating user status.
 */
export type UpdateUserStatusDTO = {
  status: UserStatus;
};
