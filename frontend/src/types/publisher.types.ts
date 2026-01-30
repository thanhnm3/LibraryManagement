/**
 * Minimal book reference for PublisherDetailDTO (replace with BookDTO from book.types when available).
 */
type BookReference = {
  id: number;
  title: string;
  isbn: string;
};

/**
 * Publisher DTO from backend.
 */
export type PublisherDTO = {
  id: number;
  name: string;
  website: string | null;
  address: string | null;
};

/**
 * Publisher detail with createdAt and books list.
 */
export type PublisherDetailDTO = PublisherDTO & {
  createdAt: string;
  books: BookReference[];
};

/**
 * Request body for creating a publisher.
 */
export type PublisherRequestDTO = {
  name: string;
  website?: string | null;
  address?: string | null;
};

/**
 * Request body for updating a publisher (all fields optional).
 */
export type PublisherUpdateDTO = {
  name?: string;
  website?: string | null;
  address?: string | null;
};
