package com.example.demo.service.publisher;

import com.example.demo.dto.publisher.PublisherDTO;
import com.example.demo.dto.publisher.PublisherDetailDTO;
import com.example.demo.dto.publisher.PublisherRequestDTO;
import com.example.demo.dto.publisher.PublisherUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Publisher operations
 */
public interface PublisherService {

	/**
	 * Tạo nhà xuất bản mới
	 * 
	 * @param requestDTO - Thông tin nhà xuất bản
	 * @return PublisherDTO
	 */
	PublisherDTO createPublisher(PublisherRequestDTO requestDTO);

	/**
	 * Lấy danh sách nhà xuất bản
	 * 
	 * @param pageable - Thông tin phân trang
	 * @return Page<PublisherDTO>
	 */
	Page<PublisherDTO> getAllPublishers(Pageable pageable);

	/**
	 * Lấy chi tiết nhà xuất bản
	 * 
	 * @param publisherId - ID nhà xuất bản
	 * @return PublisherDetailDTO
	 */
	PublisherDetailDTO getPublisherById(Long publisherId);

	/**
	 * Cập nhật thông tin nhà xuất bản
	 * 
	 * @param publisherId - ID nhà xuất bản
	 * @param updateDTO   - Thông tin cập nhật
	 * @return PublisherDTO
	 */
	PublisherDTO updatePublisher(Long publisherId, PublisherUpdateDTO updateDTO);

	/**
	 * Xóa nhà xuất bản
	 * 
	 * @param publisherId - ID nhà xuất bản
	 */
	void deletePublisher(Long publisherId);
}
