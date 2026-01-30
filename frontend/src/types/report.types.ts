import type { AuthorDTO } from './author.types';
import type { BookDTO } from './book.types';

/**
 * Dashboard statistics DTO (totals, active/overdue loans, most borrowed, top authors).
 */
export type DashboardStatisticsDTO = {
  totalBooks: number;
  totalUsers: number;
  activeLoans: number;
  overdueLoans: number;
  mostBorrowedBooks: BookDTO[];
  topAuthors: AuthorDTO[];
};
