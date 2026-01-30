package com.example.demo.controller;

import com.example.demo.dto.loan.LoanReportDTO;
import com.example.demo.dto.report.DashboardStatisticsDTO;
import com.example.demo.dto.review.ReviewReportDTO;
import com.example.demo.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * UC-REPORT-001: Báo cáo thống kê tổng quan (Admin)
	 */
	@GetMapping("/dashboard")
	public ResponseEntity<DashboardStatisticsDTO> getDashboardStatistics() {
		DashboardStatisticsDTO statistics = reportService.getDashboardStatistics();
		return ResponseEntity.ok(statistics);
	}

	/**
	 * UC-REPORT-002: Báo cáo hoạt động mượn trả theo thời gian
	 */
	@GetMapping("/loans")
	public ResponseEntity<LoanReportDTO> getLoanReport(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		LoanReportDTO report = reportService.getLoanReport(startDate, endDate);
		return ResponseEntity.ok(report);
	}

	/**
	 * UC-REPORT-003: Báo cáo đánh giá sách
	 */
	@GetMapping("/reviews")
	public ResponseEntity<ReviewReportDTO> getReviewReport(@RequestParam(required = false) Long bookId) {
		ReviewReportDTO report = reportService.getReviewReport(bookId);
		return ResponseEntity.ok(report);
	}
}
