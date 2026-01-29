package com.example.demo.mapper;

import com.example.demo.dto.loan.LoanDTO;
import com.example.demo.dto.loan.LoanDetailDTO;
import com.example.demo.dto.loan.LoanRequestDTO;
import com.example.demo.entity.Loan;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Loan entity and DTOs
 */
@Component
public class LoanMapper {

	private final UserMapper userMapper;
	private final BookMapper bookMapper;

	public LoanMapper(UserMapper userMapper, BookMapper bookMapper) {
		this.userMapper = userMapper;
		this.bookMapper = bookMapper;
	}

	/**
	 * Convert Loan entity to LoanDTO
	 * 
	 * @param loan - Loan entity
	 * @return LoanDTO
	 */
	public LoanDTO toDTO(Loan loan) {
		if (loan == null) {
			return null;
		}

		LoanDTO dto = new LoanDTO();
		dto.setId(loan.getId());
		dto.setUserId(loan.getUser().getId());
		dto.setUserFullName(loan.getUser().getFullName());
		dto.setBookId(loan.getBook().getId());
		dto.setBookTitle(loan.getBook().getTitle());
		dto.setBorrowDate(loan.getBorrowDate());
		dto.setDueDate(loan.getDueDate());
		dto.setReturnDate(loan.getReturnDate());
		dto.setStatus(loan.getStatus());
		return dto;
	}

	/**
	 * Convert Loan entity to LoanDetailDTO
	 * 
	 * @param loan - Loan entity
	 * @return LoanDetailDTO
	 */
	public LoanDetailDTO toDetailDTO(Loan loan) {
		if (loan == null) {
			return null;
		}

		LoanDetailDTO dto = new LoanDetailDTO();
		dto.setId(loan.getId());
		dto.setBorrowDate(loan.getBorrowDate());
		dto.setDueDate(loan.getDueDate());
		dto.setReturnDate(loan.getReturnDate());
		dto.setStatus(loan.getStatus());
		dto.setUser(userMapper.toDTO(loan.getUser()));
		dto.setBook(bookMapper.toDTO(loan.getBook()));
		return dto;
	}

	/**
	 * Convert LoanRequestDTO to Loan entity
	 * Note: user and book should be set separately in service
	 * 
	 * @param requestDTO - LoanRequestDTO
	 * @return Loan entity
	 */
	public Loan toEntity(LoanRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}

		Loan loan = new Loan();
		loan.setDueDate(requestDTO.getDueDate());
		return loan;
	}
}
