package com.iglu.iglulivery.entities;

import com.iglu.iglulivery.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String deliveryAddress;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Double deliveryLatitude;
    private Double deliveryLongitude;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User deliveryPerson;
}
