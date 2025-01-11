package com.boersenparty.v_1_1.service;

import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderCleanupService {

    private final OrderRepository orderRepository;

    public OrderCleanupService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void deleteExpiredOrders() {
        LocalDateTime now = LocalDateTime.now();
        List<Order> expiredOrders = orderRepository.findByExpiresAtBefore(now);

        if (!expiredOrders.isEmpty()) {
            orderRepository.deleteAll(expiredOrders);
            System.out.println("Deleted expired orders: " + expiredOrders.size());
        }
    }
}

