package com.example.demo.service.publisher;

import com.example.demo.dto.publisher.PublisherDTO;
import com.example.demo.dto.publisher.PublisherDetailDTO;
import com.example.demo.dto.publisher.PublisherRequestDTO;
import com.example.demo.dto.publisher.PublisherUpdateDTO;
import com.example.demo.entity.Publisher;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.PublisherMapper;
import com.example.demo.repository.PublisherRepository;
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
 * Test class for PublisherService implementation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PublisherService Tests")
class PublisherServiceImplTest {

  @Mock
  private PublisherRepository publisherRepository;

  @Mock
  private PublisherMapper publisherMapper;

  @InjectMocks
  private PublisherServiceImpl publisherService;

  private PublisherRequestDTO publisherRequestDTO;
  private Publisher publisher;
  private PublisherDTO publisherDTO;
  private PublisherDetailDTO publisherDetailDTO;
  private PublisherUpdateDTO publisherUpdateDTO;
  private Long publisherId;

  @BeforeEach
  void setUp() {
    publisherId = 1L;
    publisherRequestDTO = TestDataBuilder.createPublisherRequestDTO();
    publisher = TestDataBuilder.createPublisher();
    publisherDTO = TestDataBuilder.createPublisherDTO();
    publisherDetailDTO = TestDataBuilder.createPublisherDetailDTO();
    publisherUpdateDTO = TestDataBuilder.createPublisherUpdateDTO();
  }

  @Test
  @DisplayName("Should create publisher when valid request")
  void shouldCreatePublisher_WhenValidRequest() {
    // Arrange
    when(publisherMapper.toEntity(publisherRequestDTO)).thenReturn(publisher);
    when(publisherRepository.save(publisher)).thenReturn(publisher);
    when(publisherMapper.toDTO(publisher)).thenReturn(publisherDTO);

    // Act
    PublisherDTO result = publisherService.createPublisher(publisherRequestDTO);

    // Assert
    assertNotNull(result);
    assertEquals(publisherDTO.getId(), result.getId());
    verify(publisherMapper, times(1)).toEntity(publisherRequestDTO);
    verify(publisherRepository, times(1)).save(publisher);
    verify(publisherMapper, times(1)).toDTO(publisher);
  }

  @Test
  @DisplayName("Should get all publishers with pagination")
  void shouldGetAllPublishers_WithPagination() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<Publisher> publishers = List.of(publisher);
    Page<Publisher> publisherPage = new PageImpl<>(publishers, pageable, 1);

    when(publisherRepository.findAll(pageable)).thenReturn(publisherPage);
    when(publisherMapper.toDTO(publisher)).thenReturn(publisherDTO);

    // Act
    Page<PublisherDTO> result = publisherService.getAllPublishers(pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(publisherRepository, times(1)).findAll(pageable);
    verify(publisherMapper, times(1)).toDTO(publisher);
  }

  @Test
  @DisplayName("Should get publisher by id when exists")
  void shouldGetPublisherById_WhenExists() {
    // Arrange
    when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(publisher));
    when(publisherMapper.toDetailDTO(publisher)).thenReturn(publisherDetailDTO);

    // Act
    PublisherDetailDTO result = publisherService.getPublisherById(publisherId);

    // Assert
    assertNotNull(result);
    assertEquals(publisherDetailDTO.getId(), result.getId());
    verify(publisherRepository, times(1)).findById(publisherId);
    verify(publisherMapper, times(1)).toDetailDTO(publisher);
  }

  @Test
  @DisplayName("Should throw exception when publisher not found")
  void shouldThrowException_WhenPublisherNotFound() {
    // Arrange
    when(publisherRepository.findById(publisherId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      publisherService.getPublisherById(publisherId);
    });

    verify(publisherRepository, times(1)).findById(publisherId);
    verify(publisherMapper, never()).toDetailDTO(any());
  }

  @Test
  @DisplayName("Should update publisher when valid request")
  void shouldUpdatePublisher_WhenValidRequest() {
    // Arrange
    when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(publisher));
    when(publisherRepository.save(publisher)).thenReturn(publisher);
    when(publisherMapper.toDTO(publisher)).thenReturn(publisherDTO);

    // Act
    PublisherDTO result = publisherService.updatePublisher(publisherId, publisherUpdateDTO);

    // Assert
    assertNotNull(result);
    verify(publisherRepository, times(1)).findById(publisherId);
    verify(publisherMapper, times(1)).updateEntityFromDTO(publisher, publisherUpdateDTO);
    verify(publisherRepository, times(1)).save(publisher);
    verify(publisherMapper, times(1)).toDTO(publisher);
  }

  @Test
  @DisplayName("Should throw exception when publisher not found for update")
  void shouldThrowException_WhenPublisherNotFoundForUpdate() {
    // Arrange
    when(publisherRepository.findById(publisherId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> {
      publisherService.updatePublisher(publisherId, publisherUpdateDTO);
    });

    verify(publisherRepository, times(1)).findById(publisherId);
    verify(publisherMapper, never()).updateEntityFromDTO(any(), any());
    verify(publisherRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should delete publisher when no books")
  void shouldDeletePublisher_WhenNoBooks() {
    // Arrange
    publisher.setBooks(new ArrayList<>());
    when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(publisher));

    // Act
    publisherService.deletePublisher(publisherId);

    // Assert
    verify(publisherRepository, times(1)).findById(publisherId);
    verify(publisherRepository, times(1)).delete(publisher);
  }

  @Test
  @DisplayName("Should throw exception when publisher has books")
  void shouldThrowException_WhenPublisherHasBooks() {
    // Arrange
    List<com.example.demo.entity.Book> books = List.of(TestDataBuilder.createBook());
    publisher.setBooks(books);
    when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(publisher));

    // Act & Assert
    assertThrows(BusinessRuleException.class, () -> {
      publisherService.deletePublisher(publisherId);
    });

    verify(publisherRepository, times(1)).findById(publisherId);
    verify(publisherRepository, never()).delete(any());
  }
}
