package com.example.demo.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AverageRatingDTO {

	private Long bookId;
	private String bookTitle;
	private Double averageRating;
	private Long totalReviews;
}
