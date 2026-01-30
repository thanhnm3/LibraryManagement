package com.example.demo.dto.author;

import com.example.demo.dto.book.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDetailDTO {

	private Long id;
	private String fullName;
	private String bio;
	private LocalDateTime createdAt;
	private List<BookDTO> books = new ArrayList<>();
}
