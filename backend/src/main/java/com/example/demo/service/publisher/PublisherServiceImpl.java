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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for Publisher operations
 */
@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {

	private final PublisherRepository publisherRepository;
	private final PublisherMapper publisherMapper;

	public PublisherServiceImpl(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
		this.publisherRepository = publisherRepository;
		this.publisherMapper = publisherMapper;
	}

	@Override
	public PublisherDTO createPublisher(PublisherRequestDTO requestDTO) {
		Publisher publisher = publisherMapper.toEntity(requestDTO);
		Publisher savedPublisher = publisherRepository.save(publisher);
		return publisherMapper.toDTO(savedPublisher);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PublisherDTO> getAllPublishers(Pageable pageable) {
		return publisherRepository.findAll(pageable).map(publisherMapper::toDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public PublisherDetailDTO getPublisherById(Long publisherId) {
		Publisher publisher = publisherRepository.findById(publisherId)
				.orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + publisherId));
		return publisherMapper.toDetailDTO(publisher);
	}

	@Override
	public PublisherDTO updatePublisher(Long publisherId, PublisherUpdateDTO updateDTO) {
		Publisher publisher = publisherRepository.findById(publisherId)
				.orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + publisherId));
		publisherMapper.updateEntityFromDTO(publisher, updateDTO);
		Publisher updatedPublisher = publisherRepository.save(publisher);
		return publisherMapper.toDTO(updatedPublisher);
	}

	@Override
	public void deletePublisher(Long publisherId) {
		Publisher publisher = publisherRepository.findById(publisherId)
				.orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + publisherId));

		if (!publisher.getBooks().isEmpty()) {
			throw new BusinessRuleException("Cannot delete publisher with id: " + publisherId + " because it has associated books");
		}

		publisherRepository.delete(publisher);
	}
}
