package com.boersenparty.v_1_1.mapper;

import com.boersenparty.v_1_1.dto.OrderDTO;
import com.boersenparty.v_1_1.dto.OrderItemDTO;
import com.boersenparty.v_1_1.models.Order;
import com.boersenparty.v_1_1.models.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDTOMapper {

        public OrderDTO mapToOrderDTO(Order order) {
            OrderDTO dto = new OrderDTO();
            //dto.setUuid(order.getUuid());
            dto.setId(order.getId());

            dto.setBelongs_to(order.getBelongsTo());
            dto.setItems(order.getOrderItems().stream().map(this::mapToOrderItemDTO).collect(Collectors.toList()));
            dto.setTotalPrice(order.getTotalPrice());
            dto.setPaid(order.getIs_paid());
            return dto;
        }

    public OrderItemDTO mapToOrderItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setPricePerItem(item.getPricePerItem());
        dto.setQuantity(item.getQuantity());
        dto.setTotalItemPrice(item.getTotalItemPrice());
        return dto;
    }


    public List<OrderDTO> mapToOrderDTOList(List<Order> orders) {
            return orders.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
        }
    }


