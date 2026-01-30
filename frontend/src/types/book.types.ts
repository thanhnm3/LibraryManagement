import type { AuthorDTO } from './author.types';
import type { CategoryDTO } from './category.types';
import type { PublisherDTO } from './publisher.types';
import type { ReviewSummaryDTO } from './review.types';

/**
 * Book list/summary DTO from API.
 */
export type BookDTO = {
  id: number;
  title: string;
  isbn: string;
  publicationYear: number;
  description: string | null;
  coverImageUrl: string | null;
  filePath: string | null;
  publisher: PublisherDTO;
  authors: AuthorDTO[];
  categories: CategoryDTO[];
};

/**
 * Book detail DTO including review summary (authors, categories, reviewSummary).
 */
export type BookDetailDTO = BookDTO & {
  reviewSummary?: ReviewSummaryDTO;
};

/**
 * Request body for creating a book.
 */
export type BookRequestDTO = {
  title: string;
  isbn: string;
  publicationYear: number;
  description?: string;
  coverImageUrl?: string;
  filePath?: string;
  publisherId: number;
  authorIds?: number[];
  categoryIds?: number[];
};

/**
 * Request body for updating a book (all fields optional).
 */
export type BookUpdateDTO = {
  title?: string;
  isbn?: string;
  publicationYear?: number;
  description?: string;
  coverImageUrl?: string;
  filePath?: string;
  publisherId?: number;
  authorIds?: number[];
  categoryIds?: number[];
};

/**
 * Search criteria for books (query params / POST body).
 */
export type BookSearchCriteriaDTO = {
  title?: string;
  isbn?: string;
  author?: string;
  category?: string;
  publisher?: string;
  minYear?: number;
  maxYear?: number;
};
