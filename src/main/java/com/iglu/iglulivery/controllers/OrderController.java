package com.iglu.iglulivery.controllers;

import com.iglu.iglulivery.dto.OrderDTO;
import com.iglu.iglulivery.entities.Order;
import com.iglu.iglulivery.entities.User;
import com.iglu.iglulivery.servicies.OrderService;
import com.iglu.iglulivery.servicies.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO request) {
        OrderDTO newOrder = orderService.createOrder(request);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAvailableOrders() {
        List<OrderDTO> orders = orderService.getAvailableOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<OrderDTO> acceptOrder(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.name();
        OrderDTO updatedOrder = orderService.assignOrder(id, userEmail);
        return ResponseEntity.ok(updatedOrder);
    }
}

@Data
class UpdateStatusOrder {
    private String status;
    private Double latitude;
    private Double longitude;
}
