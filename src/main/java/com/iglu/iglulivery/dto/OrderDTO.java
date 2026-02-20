package com.iglu.iglulivery.dto;

import com.iglu.iglulivery.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private String customerName;
    private String deliveryAddress;
    private OrderStatus status;
    private Double deliveryLatitude;
    private Double deliveryLongitude;
}
