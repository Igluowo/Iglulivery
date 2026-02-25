package com.iglu.iglulivery.services;

import com.iglu.iglulivery.dto.OrderDTO;
import com.iglu.iglulivery.entities.Order;
import com.iglu.iglulivery.entities.User;
import com.iglu.iglulivery.enums.OrderStatus;
import com.iglu.iglulivery.repositories.OrderRepository;
import com.iglu.iglulivery.repositories.UserRepository;
import com.iglu.iglulivery.servicies.OrderService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUpSecurityContext() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("test@iglulivery.com",
                null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() {
        Order order1 = new Order(); order1.setId(1L); order1.setStatus(OrderStatus.PENDING);
        Order order2 = new Order(); order2.setId(2L); order2.setStatus(OrderStatus.ACCEPTED);
        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));
        List<OrderDTO> result = orderService.getAllOrders();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void createOrder_WithValidRequest_ShouldSaveAndReturnOrder() {
        OrderDTO request = new OrderDTO();
        request.setCustomerName("Juan");
        request.setDeliveryAddress("Calle 123");
        User mockUser = new User();
        mockUser.setEmail("test@iglulivery.com");
        Order savedOrder = new Order();
        savedOrder.setId(10L);
        savedOrder.setCustomerName("Juan");
        savedOrder.setStatus(OrderStatus.PENDING);
        when(userRepository.findByEmail("test@iglulivery.com")).thenReturn(Optional.of(mockUser));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        OrderDTO result = orderService.createOrder(request);
        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals("Juan", result.getCustomerName());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrderById_WhenOrderDosNotExist_ShouldThrowException() {
        Long invalidId = 999L;
        when(orderRepository.findById(invalidId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
           orderService.getOrderById(invalidId);
        });
        assertTrue(exception.getMessage().contains("not found"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void acceptOrder_WhenOrderExists_ShouldReturnAcceptedStatus() {
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setStatus(OrderStatus.PENDING);
        User mockUser = new User();
        mockUser.setEmail("test@iglulivery.com");
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(userRepository.findByEmail("test@iglulivery.com")).thenReturn(Optional.of(mockUser));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        OrderDTO result = orderService.assignOrder(orderId);
        assertNotNull(result);
        assertEquals(OrderStatus.ACCEPTED, result.getStatus());
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void getAvaiableOrders_ShouldReturnOnlyPendingOrders() {
        Order pendingOrder1 = new Order();
        pendingOrder1.setId(1L);
        pendingOrder1.setStatus(OrderStatus.PENDING);
        Order pendingOrder2 = new Order();
        pendingOrder2.setId(2L);
        pendingOrder2.setStatus(OrderStatus.PENDING);
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(List.of(pendingOrder1, pendingOrder2));
        List<OrderDTO> result = orderService.getAvailableOrders();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findByStatus(OrderStatus.PENDING);
    }

    @Test
    void updateOrderStatus_WithValidAndStatus_ShouldUpdateAndReturnOrder() {
        Long orderId = 1L;
        String newStatus = "accepted";
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        OrderDTO result = orderService.updateOrderStatus(orderId, newStatus);
        assertNotNull(result);
        assertEquals(OrderStatus.ACCEPTED, result.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void acceptOrder_WhenOrderDoesNotExist_ShouldThrowException() {
        Long orderId = 99L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.assignOrder(orderId);
        });
        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }
}

