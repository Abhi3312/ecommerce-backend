package com.ecommerce.backend.report.service.impl;

import com.ecommerce.backend.report.dto.SalesSummaryResponse;
import com.ecommerce.backend.report.dto.TopProductResponse;
import com.ecommerce.backend.report.service.ReportService;
import com.ecommerce.backend.order.model.OrderStatus;
import com.ecommerce.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderRepository orderRepository;

    @Override
    public SalesSummaryResponse getSalesSummary(LocalDate from, LocalDate to) {

        LocalDateTime fromDate = from != null ? from.atStartOfDay() : null;
        LocalDateTime toDate = to != null ? to.atTime(23, 59, 59) : null;

        return orderRepository.getSalesSummary(
                List.of(OrderStatus.PAID, OrderStatus.DELIVERED),
                fromDate,
                toDate
        );
    }

    @Override
    public List<TopProductResponse> getTopSellingProducts(int limit) {

        return orderRepository.findTopSellingProducts(
                        List.of(OrderStatus.PAID, OrderStatus.DELIVERED)
                )
                .stream()
                .limit(limit)
                .toList();
    }
}
