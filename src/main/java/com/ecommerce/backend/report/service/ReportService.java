package com.ecommerce.backend.report.service;

import com.ecommerce.backend.report.dto.SalesSummaryResponse;
import com.ecommerce.backend.report.dto.TopProductResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    SalesSummaryResponse getSalesSummary(LocalDate from, LocalDate to);

    List<TopProductResponse> getTopSellingProducts(int limit);
}
