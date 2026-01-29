package com.example.demo.mapper;

import com.example.demo.dto.category.CategoryDTO;
import com.example.demo.dto.category.CategoryDetailDTO;
import com.example.demo.dto.category.CategoryRequestDTO;
import com.example.demo.dto.category.CategoryUpdateDTO;
import com.example.demo.entity.Category;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper class for converting between Category entity and DTOs
 */
@Component
public class CategoryMapper {

	private final BookMapper bookMapper;

	public CategoryMapper(@Lazy BookMapper bookMapper) {
		this.bookMapper = bookMapper;
	}

	/**
	 * Convert Category entity to CategoryDTO
	 * 
	 * @param category - Category entity
	 * @return CategoryDTO
	 */
	public CategoryDTO toDTO(Category category) {
		if (category == null) {
			return null;
		}

		CategoryDTO dto = new CategoryDTO();
		dto.setId(category.getId());
		dto.setName(category.getName());
		dto.setDescription(category.getDescription());
		return dto;
	}

	/**
	 * Convert Category entity to CategoryDetailDTO
	 * 
	 * @param category - Category entity
	 * @return CategoryDetailDTO
	 */
	public CategoryDetailDTO toDetailDTO(Category category) {
		if (category == null) {
			return null;
		}

		CategoryDetailDTO dto = new CategoryDetailDTO();
		dto.setId(category.getId());
		dto.setName(category.getName());
		dto.setDescription(category.getDescription());
		dto.setBooks(category.getBooks().stream()
				.map(bookMapper::toDTO)
				.collect(Collectors.toList()));
		return dto;
	}

	/**
	 * Convert CategoryRequestDTO to Category entity
	 * 
	 * @param requestDTO - CategoryRequestDTO
	 * @return Category entity
	 */
	public Category toEntity(CategoryRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}

		Category category = new Category();
		category.setName(requestDTO.getName());
		category.setDescription(requestDTO.getDescription());
		return category;
	}

	/**
	 * Update Category entity from CategoryUpdateDTO
	 * 
	 * @param category - Category entity to update
	 * @param updateDTO - CategoryUpdateDTO with new values
	 */
	public void updateEntityFromDTO(Category category, CategoryUpdateDTO updateDTO) {
		if (category == null || updateDTO == null) {
			return;
		}

		if (updateDTO.getName() != null) {
			category.setName(updateDTO.getName());
		}

		if (updateDTO.getDescription() != null) {
			category.setDescription(updateDTO.getDescription());
		}
	}
}
