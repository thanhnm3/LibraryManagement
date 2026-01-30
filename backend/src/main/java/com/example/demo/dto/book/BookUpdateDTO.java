package com.example.demo.dto.book;

import jakarta.validation.constraints.Min;
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
public class BookUpdateDTO {

	private String title;
	private String isbn;
	
	@Min(value = 1000, message = "Publication year must be greater than 1000")
	private Integer publicationYear;

	private String description;
	private String coverImageUrl;
	private String filePath;
	private Long publisherId;
	private List<Long> authorIds = new ArrayList<>();
	private List<Long> categoryIds = new ArrayList<>();
}
