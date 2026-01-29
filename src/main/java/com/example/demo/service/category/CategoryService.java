package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryDTO;
import com.example.demo.dto.category.CategoryDetailDTO;
import com.example.demo.dto.category.CategoryRequestDTO;
import com.example.demo.dto.category.CategoryUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Category operations
 */
public interface CategoryService {

	/**
	 * Tạo danh mục mới
	 * 
	 * @param requestDTO - Thông tin danh mục
	 * @return CategoryDTO
	 */
	CategoryDTO createCategory(CategoryRequestDTO requestDTO);

	/**
	 * Lấy danh sách danh mục
	 * 
	 * @param pageable - Thông tin phân trang
	 * @return Page<CategoryDTO>
	 */
	Page<CategoryDTO> getAllCategories(Pageable pageable);

	/**
	 * Lấy chi tiết danh mục
	 * 
	 * @param categoryId - ID danh mục
	 * @return CategoryDetailDTO
	 */
	CategoryDetailDTO getCategoryById(Long categoryId);

	/**
	 * Cập nhật thông tin danh mục
	 * 
	 * @param categoryId - ID danh mục
	 * @param updateDTO  - Thông tin cập nhật
	 * @return CategoryDTO
	 */
	CategoryDTO updateCategory(Long categoryId, CategoryUpdateDTO updateDTO);

	/**
	 * Xóa danh mục
	 * 
	 * @param categoryId - ID danh mục
	 */
	void deleteCategory(Long categoryId);
}
