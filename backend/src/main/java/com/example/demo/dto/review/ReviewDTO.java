package com.example.demo.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

	private Long id;
	private Long userId;
	private String userFullName;
	private Long bookId;
	private String bookTitle;
	private Integer rating;
	private String comment;
	private LocalDateTime createdAt;
}
