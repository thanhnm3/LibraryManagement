import type { BookSearchCriteriaDTO } from './book.types';

/**
 * Query params for GET /api/search/advanced.
 */
export type AdvancedSearchParams = {
  categoryName?: string;
  authorName?: string;
  userId?: string;
  title?: string;
};

export type { BookSearchCriteriaDTO } from './book.types';
