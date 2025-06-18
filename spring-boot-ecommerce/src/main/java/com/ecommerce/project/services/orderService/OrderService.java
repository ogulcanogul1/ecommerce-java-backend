package com.ecommerce.project.services.orderService;

import com.ecommerce.project.dtos.orderDto.OrderDto;
import com.ecommerce.project.result.ServiceResult;

public interface OrderService {
    ServiceResult<OrderDto> createOrder(Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
