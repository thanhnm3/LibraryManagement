/**
 * Minimal book reference for AuthorDetailDTO (replace with BookDTO from book.types when available).
 */
type BookReference = {
  id: number;
  title: string;
  isbn: string;
};

/**
 * Author DTO from backend.
 */
export type AuthorDTO = {
  id: number;
  fullName: string;
  bio: string | null;
};

/**
 * Author detail with createdAt and books list.
 */
export type AuthorDetailDTO = AuthorDTO & {
  createdAt: string;
  books: BookReference[];
};

/**
 * Request body for creating an author.
 */
export type AuthorRequestDTO = {
  fullName: string;
  bio?: string | null;
};

/**
 * Request body for updating an author (all fields optional).
 */
export type AuthorUpdateDTO = {
  fullName?: string;
  bio?: string | null;
};
