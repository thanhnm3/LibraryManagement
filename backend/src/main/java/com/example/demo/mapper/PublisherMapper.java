package com.example.demo.mapper;

import com.example.demo.dto.publisher.PublisherDTO;
import com.example.demo.dto.publisher.PublisherDetailDTO;
import com.example.demo.dto.publisher.PublisherRequestDTO;
import com.example.demo.dto.publisher.PublisherUpdateDTO;
import com.example.demo.entity.Publisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper class for converting between Publisher entity and DTOs
 */
@Component
public class PublisherMapper {

	private final BookMapper bookMapper;

	public PublisherMapper(@Lazy BookMapper bookMapper) {
		this.bookMapper = bookMapper;
	}

	/**
	 * Convert Publisher entity to PublisherDTO
	 * 
	 * @param publisher - Publisher entity
	 * @return PublisherDTO
	 */
	public PublisherDTO toDTO(Publisher publisher) {
		if (publisher == null) {
			return null;
		}

		PublisherDTO dto = new PublisherDTO();
		dto.setId(publisher.getId());
		dto.setName(publisher.getName());
		dto.setWebsite(publisher.getWebsite());
		dto.setAddress(publisher.getAddress());
		return dto;
	}

	/**
	 * Convert Publisher entity to PublisherDetailDTO
	 * 
	 * @param publisher - Publisher entity
	 * @return PublisherDetailDTO
	 */
	public PublisherDetailDTO toDetailDTO(Publisher publisher) {
		if (publisher == null) {
			return null;
		}

		PublisherDetailDTO dto = new PublisherDetailDTO();
		dto.setId(publisher.getId());
		dto.setName(publisher.getName());
		dto.setWebsite(publisher.getWebsite());
		dto.setAddress(publisher.getAddress());
		dto.setCreatedAt(publisher.getCreatedAt());
		dto.setBooks(publisher.getBooks().stream()
				.map(bookMapper::toDTO)
				.collect(Collectors.toList()));
		return dto;
	}

	/**
	 * Convert PublisherRequestDTO to Publisher entity
	 * 
	 * @param requestDTO - PublisherRequestDTO
	 * @return Publisher entity
	 */
	public Publisher toEntity(PublisherRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}

		Publisher publisher = new Publisher();
		publisher.setName(requestDTO.getName());
		publisher.setWebsite(requestDTO.getWebsite());
		publisher.setAddress(requestDTO.getAddress());
		return publisher;
	}

	/**
	 * Update Publisher entity from PublisherUpdateDTO
	 * 
	 * @param publisher - Publisher entity to update
	 * @param updateDTO - PublisherUpdateDTO with new values
	 */
	public void updateEntityFromDTO(Publisher publisher, PublisherUpdateDTO updateDTO) {
		if (publisher == null || updateDTO == null) {
			return;
		}

		if (updateDTO.getName() != null) {
			publisher.setName(updateDTO.getName());
		}

		if (updateDTO.getWebsite() != null) {
			publisher.setWebsite(updateDTO.getWebsite());
		}

		if (updateDTO.getAddress() != null) {
			publisher.setAddress(updateDTO.getAddress());
		}
	}
}
