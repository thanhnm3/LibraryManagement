import type { BookDTO } from './book.types';
import type { UserDTO } from './user.types';
import type { LoanStatus } from './enums';

/**
 * Loan list/summary DTO from API.
 */
export type LoanDTO = {
  id: number;
  userId: number;
  userFullName: string;
  bookId: number;
  bookTitle: string;
  borrowDate: string;
  dueDate: string;
  returnDate: string | null;
  status: LoanStatus;
};

/**
 * Loan detail DTO with full user and book.
 */
export type LoanDetailDTO = {
  id: number;
  user: UserDTO;
  book: BookDTO;
  borrowDate: string;
  dueDate: string;
  returnDate: string | null;
  status: LoanStatus;
};

/**
 * Request body for creating a loan.
 */
export type LoanRequestDTO = {
  userId: number;
  bookId: number;
  dueDate: string;
};

/**
 * Request body for renewing a loan (new due date).
 */
export type LoanRenewalRequestDTO = {
  newDueDate: string;
};

/**
 * Loan report DTO (borrows/returns by date).
 */
export type LoanReportDTO = {
  startDate: string;
  endDate: string;
  borrowsByDate: Record<string, number>;
  returnsByDate: Record<string, number>;
  totalBorrows: number;
  totalReturns: number;
};

/**
 * Loan statistics DTO (totals, most borrowed books).
 */
export type LoanStatisticsDTO = {
  totalBorrowed: number;
  totalReturned: number;
  totalOverdue: number;
  mostBorrowedBooks: BookDTO[];
};
