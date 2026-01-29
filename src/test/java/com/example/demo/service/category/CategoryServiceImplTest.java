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
import com.example.demo.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for CategoryService implementation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService Tests")
class CategoryServiceImplTest {

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private CategoryMapper categoryMapper;

  @InjectMocks
  private CategoryServiceImpl categoryService;

  private CategoryRequestDTO categoryRequestDTO;
  private Category category;
  private CategoryDTO categoryDTO;
  private CategoryDetailDTO categoryDetailDTO;
  private CategoryUpdateDTO categoryUpdateDTO;
  private Long categoryId;

  @BeforeEach
  void setUp() {
    categoryId = 1L;
    categoryRequestDTO = TestDataBuilder.createCategoryRequestDTO();
    category = TestDataBuilder.createCategory();
    categoryDTO = TestDataBuilder.createCategoryDTO();
    categoryDetailDTO = TestDataBuilder.createCategoryDetailDTO();
    categoryUpdateDTO = TestDataBuilder.createCategoryUpdateDTO();
  }

  @Test
  @DisplayName("Should create category when valid request")
  void shouldCreateCategory_WhenValidRequest() {
    // Arrange
    when(categoryMapper.toEntity(categoryRequestDTO)).thenReturn(category);
    when(categoryRepository.save(category)).thenReturn(category);
    when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

    // Act
    CategoryDTO result = categoryService.createCategory(categoryRequestDTO);

    // Assert
    assertNotNull(result);
    assertEquals(categoryDTO.getId(), result.getId());
    verify(categoryMapper, times(1)).toEntity(categoryRequestDTO);
    verify(categoryRepository, times(1)).save(category);
    verify(categoryMapper, times(1)).toDTO(category);
  }

  @Test
  @DisplayName("Should get all categories with pagination")
  void shouldGetAllCategories_WithPagination() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<Category> categories = List.of(category);
    Page<Category> categoryPage = new PageImpl<>(categories, pageable, 1);

    when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
    when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

    // Act
    Page<CategoryDTO> result = categoryService.getAllCategories(pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(categoryRepository, times(1)).findAll(pageable);
    verify(categoryMapper, times(1)).toDTO(category);
  }

  @Test
  @DisplayName("Should get category by id when exists")
  void shouldGetCategoryById_WhenExists() {
    // Arrange
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    when(categoryMapper.toDetailDTO(category)).thenReturn(categoryDetailDTO);

    // Act
    CategoryDetailDTO result = categoryService.getCategoryById(categoryId);

    // Assert
    assertNotNull(result);
    assertEquals(categoryDetailDTO.getId(), result.getId());
    verify(categoryRepository, times(1)).findById(categoryId);
    verify(categoryMapper, times(1)).toDetailDTO(category);
  }

  @Test
  @DisplayName("Should throw exception when category not found")
  void shouldThrowException_WhenCategoryNotFound() {
    // Arrange
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      categoryService.getCategoryById(categoryId);
    });

    verify(categoryRepository, times(1)).findById(categoryId);
    verify(categoryMapper, never()).toDetailDTO(any());
  }

  @Test
  @DisplayName("Should update category when valid request")
  void shouldUpdateCategory_WhenValidRequest() {
    // Arrange
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    when(categoryRepository.save(category)).thenReturn(category);
    when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

    // Act
    CategoryDTO result = categoryService.updateCategory(categoryId, categoryUpdateDTO);

    // Assert
    assertNotNull(result);
    verify(categoryRepository, times(1)).findById(categoryId);
    verify(categoryMapper, times(1)).updateEntityFromDTO(category, categoryUpdateDTO);
    verify(categoryRepository, times(1)).save(category);
    verify(categoryMapper, times(1)).toDTO(category);
  }

  @Test
  @DisplayName("Should throw exception when category not found for update")
  void shouldThrowException_WhenCategoryNotFoundForUpdate() {
    // Arrange
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      categoryService.updateCategory(categoryId, categoryUpdateDTO);
    });

    verify(categoryRepository, times(1)).findById(categoryId);
    verify(categoryMapper, never()).updateEntityFromDTO(any(), any());
    verify(categoryRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should delete category when no books")
  void shouldDeleteCategory_WhenNoBooks() {
    // Arrange
    category.setBooks(new ArrayList<>());
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

    // Act
    categoryService.deleteCategory(categoryId);

    // Assert
    verify(categoryRepository, times(1)).findById(categoryId);
    verify(categoryRepository, times(1)).delete(category);
  }

  @Test
  @DisplayName("Should throw exception when category has books")
  void shouldThrowException_WhenCategoryHasBooks() {
    // Arrange
    List<com.example.demo.entity.Book> books = List.of(TestDataBuilder.createBook());
    category.setBooks(books);
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

    // Act & Assert
    assertThrows(BusinessRuleException.class, () -> {
      categoryService.deleteCategory(categoryId);
    });

    verify(categoryRepository, times(1)).findById(categoryId);
    verify(categoryRepository, never()).delete(any());
  }
}
