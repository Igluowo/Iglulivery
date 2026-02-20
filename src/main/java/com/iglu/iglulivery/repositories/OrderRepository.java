package com.iglu.iglulivery.repositories;

import com.iglu.iglulivery.entities.Order;
import com.iglu.iglulivery.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
}
