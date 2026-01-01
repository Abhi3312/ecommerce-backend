package com.ecommerce.backend.report.controller;

import com.ecommerce.backend.report.dto.SalesSummaryResponse;
import com.ecommerce.backend.report.dto.TopProductResponse;
import com.ecommerce.backend.common.response.ApiResponse;
import com.ecommerce.backend.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    public ResponseEntity<ApiResponse<SalesSummaryResponse>> getSalesSummary(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) {

        SalesSummaryResponse response =
                reportService.getSalesSummary(from, to);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Sales summary fetched successfully", response)
        );
    }

    @GetMapping("/top-products")
    public ResponseEntity<ApiResponse<List<TopProductResponse>>> getTopProducts(
            @RequestParam(defaultValue = "5") int limit
    ) {

        List<TopProductResponse> response =
                reportService.getTopSellingProducts(limit);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Top selling products fetched successfully", response)
        );
    }
}
