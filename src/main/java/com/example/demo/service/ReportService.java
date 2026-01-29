package com.example.demo.service;

import com.example.demo.dto.loan.LoanReportDTO;
import com.example.demo.dto.report.DashboardStatisticsDTO;
import com.example.demo.dto.review.ReviewReportDTO;

import java.time.LocalDate;

/**
 * Service interface for Report operations
 */
public interface ReportService {

	/**
	 * UC-REPORT-001: Báo cáo thống kê tổng quan (Admin)
	 * 
	 * @return DashboardStatisticsDTO - Thống kê tổng quan
	 */
	DashboardStatisticsDTO getDashboardStatistics();

	/**
	 * UC-REPORT-002: Báo cáo hoạt động mượn trả theo thời gian
	 * 
	 * @param startDate - Ngày bắt đầu
	 * @param endDate   - Ngày kết thúc
	 * @return LoanReportDTO - Báo cáo mượn trả
	 */
	LoanReportDTO getLoanReport(LocalDate startDate, LocalDate endDate);

	/**
	 * UC-REPORT-003: Báo cáo đánh giá sách
	 * 
	 * @param bookId - ID sách (optional)
	 * @return ReviewReportDTO - Báo cáo đánh giá
	 */
	ReviewReportDTO getReviewReport(Long bookId);
}
