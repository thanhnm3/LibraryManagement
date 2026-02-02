package com.example.demo.controller;

import com.example.demo.dto.category.CategoryDTO;
import com.example.demo.dto.category.CategoryDetailDTO;
import com.example.demo.dto.category.CategoryRequestDTO;
import com.example.demo.dto.category.CategoryUpdateDTO;
import com.example.demo.service.category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	/**
	 * UC-CATEGORY-001: Tạo danh mục mới
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
		CategoryDTO createdCategory = categoryService.createCategory(requestDTO);
		return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
	}

	/**
	 * UC-CATEGORY-002: Lấy danh sách danh mục
	 */
	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> getAllCategories(
		@PageableDefault(size = 20, sort = "id") Pageable pageable
	) {
		Page<CategoryDTO> categories = categoryService.getAllCategories(pageable);
		return ResponseEntity.ok(categories);
	}

	/**
	 * UC-CATEGORY-003: Lấy chi tiết danh mục
	 */
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDetailDTO> getCategoryById(@PathVariable Long id) {
		CategoryDetailDTO category = categoryService.getCategoryById(id);
		return ResponseEntity.ok(category);
	}

	/**
	 * UC-CATEGORY-004: Cập nhật thông tin danh mục
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> updateCategory(
		@PathVariable Long id,
		@Valid @RequestBody CategoryUpdateDTO updateDTO
	) {
		CategoryDTO updatedCategory = categoryService.updateCategory(id, updateDTO);
		return ResponseEntity.ok(updatedCategory);
	}

	/**
	 * UC-CATEGORY-005: Xóa danh mục
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}
}
