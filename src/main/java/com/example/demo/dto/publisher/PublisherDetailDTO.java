package com.example.demo.dto.publisher;

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
public class PublisherDetailDTO {

	private Long id;
	private String name;
	private String website;
	private String address;
	private LocalDateTime createdAt;
	private List<BookDTO> books = new ArrayList<>();
}
