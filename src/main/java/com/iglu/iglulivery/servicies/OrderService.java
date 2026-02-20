package com.iglu.iglulivery.servicies;

import com.iglu.iglulivery.dto.OrderDTO;
import com.iglu.iglulivery.entities.Order;
import com.iglu.iglulivery.entities.User;
import com.iglu.iglulivery.enums.OrderStatus;
import com.iglu.iglulivery.repositories.OrderRepository;
import com.iglu.iglulivery.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public OrderDTO createOrder(OrderDTO order) {
        Order newOrder = new Order();
        newOrder.setCustomerName(order.getCustomerName());
        newOrder.setDeliveryAddress(order.getDeliveryAddress());
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setDeliveryLongitude(order.getDeliveryLongitude());
        newOrder.setDeliveryLatitude(order.getDeliveryLatitude());
        orderRepository.save(newOrder);
        return mapToDTO(newOrder);
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return mapListToDTO(orders);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with the ID: " + id + "not found"));
        return mapToDTO(order);
    }

    public List<OrderDTO> getAvailableOrders() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.PENDING);
        return mapListToDTO(orders);
    }

    public OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with the ID: " + id + "not found"));
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderRepository.save(order);
        return mapToDTO(order);
    }

    @Transactional
    public OrderDTO assignOrder(Long orderId, String driverId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            throw new RuntimeException("Sorry! This order was take for other driver!");
        }
        User driver = userRepository.findByEmail(driverId).orElseThrow(() -> new RuntimeException("User not found"));

        order.setDeliveryPerson(driver);
        order.setStatus(OrderStatus.ACCEPTED);

        Order savedOrder = orderRepository.save(order);
        return mapToDTO(savedOrder);

    }

    //Mapper
    public OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setCustomerName(order.getCustomerName());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setDeliveryLatitude(order.getDeliveryLatitude());
        dto.setDeliveryLongitude(order.getDeliveryLongitude());
        return dto;
    }

    public List<OrderDTO> mapListToDTO(List<Order> orders) {
        List<OrderDTO> ordersDTO = new ArrayList<>();
        for (Order order : orders) {
            ordersDTO.add(mapToDTO(order));
        }
        return ordersDTO;
    }
}
