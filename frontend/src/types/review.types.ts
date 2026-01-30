/**
 * Minimal book reference for ReviewReportDTO (replace with BookDTO from book.types when available).
 */
type BookReference = {
  id: number;
  title: string;
  isbn: string;
};

/**
 * Review summary (average rating and total count).
 */
export type ReviewSummaryDTO = {
  averageRating: number;
  totalReviews: number;
};

/**
 * Review DTO from backend.
 */
export type ReviewDTO = {
  id: number;
  userId: number;
  userFullName: string;
  bookId: number;
  bookTitle: string;
  rating: number;
  comment: string | null;
  createdAt: string;
};

/**
 * Request body for creating a review (rating 1–5).
 */
export type ReviewRequestDTO = {
  userId: number;
  bookId: number;
  rating: number;
  comment?: string | null;
};

/**
 * Request body for updating a review (rating 1–5).
 */
export type ReviewUpdateDTO = {
  rating: number;
  comment?: string | null;
};

/**
 * Average rating per book.
 */
export type AverageRatingDTO = {
  bookId: number;
  bookTitle: string;
  averageRating: number;
  totalReviews: number;
};

/**
 * Review report with rating distribution and top rated books.
 */
export type ReviewReportDTO = {
  bookId: number;
  bookTitle: string;
  ratingDistribution: Record<number, number>;
  averageRating: number;
  totalReviews: number;
  topRatedBooks: BookReference[];
};
