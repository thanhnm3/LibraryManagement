package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, unique = true, length = 20)
	private String isbn;

	@Column(name = "publication_year")
	private Integer publicationYear;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(name = "cover_image_url", length = 500)
	private String coverImageUrl;

	@Column(name = "file_path", length = 500)
	private String filePath;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;

	@Builder.Default
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
	private List<Author> authors = new ArrayList<>();

	@Builder.Default
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "book_categories", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<Category> categories = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Loan> loans = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Review> reviews = new ArrayList<>();
}
