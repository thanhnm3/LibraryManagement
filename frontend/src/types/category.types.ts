/**
 * Minimal book reference for CategoryDetailDTO (replace with BookDTO from book.types when available).
 */
type BookReference = {
  id: number;
  title: string;
  isbn: string;
};

/**
 * Category DTO from backend.
 */
export type CategoryDTO = {
  id: number;
  name: string;
  description: string | null;
};

/**
 * Category detail with books list.
 */
export type CategoryDetailDTO = CategoryDTO & {
  books: BookReference[];
};

/**
 * Request body for creating a category.
 */
export type CategoryRequestDTO = {
  name: string;
  description?: string | null;
};

/**
 * Request body for updating a category (all fields optional).
 */
export type CategoryUpdateDTO = {
  name?: string;
  description?: string | null;
};
