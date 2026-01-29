package com.example.demo.dto.category;

import com.example.demo.dto.book.BookDTO;
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
public class CategoryDetailDTO {

	private Long id;
	private String name;
	private String description;
	private List<BookDTO> books = new ArrayList<>();
}
