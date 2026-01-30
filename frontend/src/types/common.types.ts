/**
 * Spring Page response shape (pagination).
 */
export type Page<T> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
};

/**
 * API error response (status, message, timestamp).
 */
export type ApiError = {
  status: number;
  message: string;
  timestamp: string;
};

/**
 * Validation error response with field-level errors.
 */
export type ValidationErrorResponse = {
  status: number;
  message: string;
  errors: Record<string, string>;
  timestamp: string;
};
