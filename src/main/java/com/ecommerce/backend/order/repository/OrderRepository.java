package com.ecommerce.backend.order.repository;

import com.ecommerce.backend.order.model.Order;
import com.ecommerce.backend.order.model.OrderStatus;
import com.ecommerce.backend.report.dto.SalesSummaryResponse;
import com.ecommerce.backend.report.dto.TopProductResponse;
import com.ecommerce.backend.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    Page<Order> findByUser(User user, Pageable pageable);

    boolean existsByUserIdAndItemsProductId(Long userId, Long productId);


    @Query("""
    SELECT new com.ecommerce.backend.report.dto.SalesSummaryResponse(
        COUNT(o.id),
        COALESCE(SUM(o.totalAmount), java.math.BigDecimal.ZERO)
    )
    FROM Order o
    WHERE o.status IN :statuses
      AND (:from IS NULL OR o.createdAt >= :from)
      AND (:to IS NULL OR o.createdAt <= :to)
""")
    SalesSummaryResponse getSalesSummary(
            @Param("statuses") List<OrderStatus> statuses,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );


    @Query("""
        SELECT new com.ecommerce.backend.report.dto.TopProductResponse(
            oi.productName,
            SUM(oi.quantity),
            SUM(oi.price * oi.quantity)
        )
        FROM OrderItem oi
        JOIN oi.order o
        WHERE o.status IN :statuses
        GROUP BY oi.productId, oi.productName
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<TopProductResponse> findTopSellingProducts(
            @Param("statuses") List<OrderStatus> statuses
    );

}
