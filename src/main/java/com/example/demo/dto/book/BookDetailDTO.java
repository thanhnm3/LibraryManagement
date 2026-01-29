package com.example.demo.dto.book;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.category.CategoryDTO;
import com.example.demo.dto.publisher.PublisherDTO;
import com.example.demo.dto.review.ReviewSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailDTO {

	private Long id;
	private String title;
	private String isbn;
	private Integer publicationYear;
	private String description;
	private String coverImageUrl;
	private String filePath;
	private PublisherDTO publisher;
	private List<AuthorDTO> authors = new ArrayList<>();
	private List<CategoryDTO> categories = new ArrayList<>();
	private ReviewSummaryDTO reviewSummary;
}
