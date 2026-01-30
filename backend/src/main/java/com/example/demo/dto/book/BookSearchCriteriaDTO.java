package com.example.demo.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchCriteriaDTO {

	private String title;
	private String isbn;
	private String author;
	private String category;
	private String publisher;
	private Integer minYear;
	private Integer maxYear;
}
