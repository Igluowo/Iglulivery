package com.iglu.iglulivery.controllers;

import com.iglu.iglulivery.dto.OrderDTO;
import com.iglu.iglulivery.entities.Order;
import com.iglu.iglulivery.enums.OrderStatus;
import com.iglu.iglulivery.servicies.OrderService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    @WithMockUser(username = "cliente@iglulivery.com")
    void getAllOrders_ShouldReturn200AndList() throws Exception {
        OrderDTO mockOrder = new OrderDTO();
        mockOrder.setCustomerName("Juan");
        when(orderService.getAllOrders()).thenReturn(List.of(mockOrder));
        mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Juan"));
    }

    @Test
    @WithMockUser(username = "cliente@iglulivery.com")
    void createOrder_ShouldReturn201Created() throws Exception {
        Order request = new Order();
        request.setCustomerName("Maria");
        request.setDeliveryAddress("Calle Falsa 123");
        OrderDTO responseDTO = new OrderDTO();
        responseDTO.setCustomerName("Maria");
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(responseDTO);
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("Maria"));
    }

    @Test
    void getOrderById_ShouldReturn200AndOrder() throws Exception {
        Long orderId = 1L;
        OrderDTO mockOrder = new OrderDTO();
        mockOrder.setCustomerName("Juan");
        when(orderService.getOrderById(orderId)).thenReturn(mockOrder);

        mockMvc.perform(get("/api/orders/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Juan"));
    }

    @Test
    void getAvailableOrders_ShouldReturn200AndList() throws Exception {
        OrderDTO mockOrder = new OrderDTO();
        mockOrder.setCustomerName("Juan");
        when(orderService.getAvailableOrders()).thenReturn(List.of(mockOrder));
        mockMvc.perform(get("/api/orders/available")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].customerName").value("Juan"));
    }

    @Test
    void acceptOrder_ShouldReturn200AndUpdateOrder() throws Exception {
        Long orderId = 1L;
        OrderDTO mockOrder = new OrderDTO();
        mockOrder.setStatus(OrderStatus.ACCEPTED);

        when(orderService.assignOrder(orderId)).thenReturn(mockOrder);
        mockMvc.perform(patch("/api/orders/{id}/accept", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    void updateOrderStatus_ShouldReturn200AndUpdateOrder() throws Exception {
        Long orderId = 1L;
        String newState = "DELIVERED";
        OrderDTO mockOrder = new OrderDTO();
        mockOrder.setStatus(OrderStatus.DELIVERED);
        when(orderService.updateOrderStatus(orderId, newState)).thenReturn(mockOrder);
        mockMvc.perform(patch("/api/orders/{id}/status", orderId)
                .param("status", newState)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }


}
