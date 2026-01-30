package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryDTO;
import com.example.demo.dto.category.CategoryDetailDTO;
import com.example.demo.dto.category.CategoryRequestDTO;
import com.example.demo.dto.category.CategoryUpdateDTO;
import com.example.demo.entity.Category;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for Category operations
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
		this.categoryRepository = categoryRepository;
		this.categoryMapper = categoryMapper;
	}

	@Override
	public CategoryDTO createCategory(CategoryRequestDTO requestDTO) {
		Category category = categoryMapper.toEntity(requestDTO);
		Category savedCategory = categoryRepository.save(category);
		return categoryMapper.toDTO(savedCategory);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CategoryDTO> getAllCategories(Pageable pageable) {
		return categoryRepository.findAll(pageable).map(categoryMapper::toDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryDetailDTO getCategoryById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
		return categoryMapper.toDetailDTO(category);
	}

	@Override
	public CategoryDTO updateCategory(Long categoryId, CategoryUpdateDTO updateDTO) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
		categoryMapper.updateEntityFromDTO(category, updateDTO);
		Category updatedCategory = categoryRepository.save(category);
		return categoryMapper.toDTO(updatedCategory);
	}

	@Override
	public void deleteCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

		if (!category.getBooks().isEmpty()) {
			throw new BusinessRuleException("Cannot delete category with id: " + categoryId + " because it has associated books");
		}

		categoryRepository.delete(category);
	}
}
