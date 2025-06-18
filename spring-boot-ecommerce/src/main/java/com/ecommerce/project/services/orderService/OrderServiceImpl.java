package com.ecommerce.project.services.orderService;

import com.ecommerce.project.dtos.orderDto.OrderDto;
import com.ecommerce.project.dtos.orderDto.OrderItemDto;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.models.*;
import com.ecommerce.project.repositories.*;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.services.cartService.CartService;
import com.ecommerce.project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final AuthUtil authUtil;

    private final CartRepository cartRepository;

    private final AddressRepository addressRepository;

    private final PaymentRepository paymentRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductRepository productRepository;

    private final CartService cartService;

    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, AuthUtil authUtil, CartRepository cartRepository, AddressRepository addressRepository, PaymentRepository paymentRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, CartService cartService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.authUtil = authUtil;
        this.cartRepository = cartRepository;
        this.addressRepository = addressRepository;
        this.paymentRepository = paymentRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ServiceResult<OrderDto> createOrder(Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {

        String email =  authUtil.loggedInEmail();

        User user = authUtil.loggedInUser();

        Cart cart = cartRepository.getCartByEmail(email);
        if (cart == null) {
            throw new APIException("Cart Not Found");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);

        Order order = new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Accepted");
        order.setAddress(address);
        order.setUser(user);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        payment.setOrder(order);

        payment = paymentRepository.save(payment);

        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            // Reduce stock quantity
            product.setQuantity(product.getQuantity() - quantity);

            // Save product back to the database
            productRepository.save(product);

            // Remove items from cart
            cartService.deleteProductFromCart(item.getProduct().getProductId());
        });

        OrderDto orderDTO = modelMapper.map(savedOrder, OrderDto.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDto.class)));

        orderDTO.setAddressId(addressId);

        return ServiceResult.success(orderDTO,"Order Created Successfully");
    }
}
