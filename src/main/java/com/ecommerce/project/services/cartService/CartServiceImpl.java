package com.ecommerce.project.services.cartService;

import com.ecommerce.project.dtos.cartDtos.CartItemDto;
import com.ecommerce.project.dtos.cartDtos.CartResponse;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.exceptions.StockNotFoundException;
import com.ecommerce.project.models.Cart;
import com.ecommerce.project.models.CartItem;
import com.ecommerce.project.models.Product;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.utils.AuthUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthUtil authUtil;

    public CartServiceImpl(ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, AuthUtil authUtil) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.authUtil = authUtil;
    }

    @Override
    public ServiceResult<CartResponse> addProductToCart(Long productId, Integer quantity) {

        // cart control - create or get cart
        Cart cart = createCart();

        // get request product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));


        // stock control

        boolean stockState = stockControl(product.getQuantity(),quantity);

        if(!stockState){
            throw new StockNotFoundException(productId,product.getProductName(),quantity);
        }


        for (CartItem cartItem:cart.getCartItems()) {
            if(cartItem.getProduct().getProductId().equals(productId)){
                throw new APIException("Product already Cart!");
            }
        }

        // new Cart Item
        CartItem cartItem = new CartItem();

        cartItem.setProduct(product);

        cartItem.setQuantity(quantity);

        cartItem.setCart(cart);

        cart.getCartItems().add(cartItem);

        cartItemRepository.save(cartItem);

        List<CartItemDto> cartItemDtos = new ArrayList<>();


        Integer productQuantity;
        Double productPrice;
        int percentageOfDiscount = 0;
        Double salesPrice = 0.0;
        Double totalSalesPrice;

        cart.setTotalPrice(0.0);
        for (CartItem item :cart.getCartItems()){
            productQuantity = item.getQuantity();
            productPrice = item.getProduct().getPrice();

            if(item.getProduct().getPercentageOfDiscount() > 0){
                percentageOfDiscount = item.getProduct().getPercentageOfDiscount();

                salesPrice =  productPrice * ((1 - percentageOfDiscount) / 100);

                totalSalesPrice = salesPrice * quantity;

                cart.setTotalPrice(cart.getTotalPrice() + totalSalesPrice);

                cartItemDtos.add(new CartItemDto(item.getProduct().getProductName(),productPrice,salesPrice,productQuantity,percentageOfDiscount,totalSalesPrice));

               continue;
            }
            totalSalesPrice = productPrice * productQuantity;
            cart.setTotalPrice(cart.getTotalPrice() + totalSalesPrice);
            cartItemDtos.add(new CartItemDto(item.getProduct().getProductName(),productPrice,productPrice,productQuantity,percentageOfDiscount,totalSalesPrice));

        }

        Cart savedCart = cartRepository.save(cart);


        return ServiceResult.success(new CartResponse(savedCart.getCartId(),cart.getTotalPrice(),cartItemDtos),"Cart Info");
    }

    @Override
    public ServiceResult<List<CartResponse>> getAllCarts(Integer pageNumber, Integer pageSize, String sortDirection,String sortBy) {

        Sort sort = sortDirection.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        if(pageNumber == null || pageNumber <= 0 || pageSize == null || pageSize <= 0){
            List<Cart> carts = cartRepository.findAll(sort);

            if(carts.isEmpty())
                throw new APIException("No Cart created yet!");

            List<CartResponse> response = cartsMap(carts);

            return ServiceResult.success(response,"All Carts");


        }

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Cart> pageCarts = cartRepository.findAll(pageable);

        if(pageCarts.isEmpty()){
            throw new APIException("No Cart created yet!");
        }

        List<Cart> carts = pageCarts.getContent();


        List<CartResponse> response = cartsMap(carts);

        return ServiceResult.success(response,"All Carts");
    }

    @Override
    public ServiceResult<CartResponse> getCartOfSignedUser() {

        Cart cart = cartRepository.getCartByEmail(authUtil.loggedInEmail());

        if(cart == null){
            throw new APIException("Cart not found!");
        }

        CartResponse response = new CartResponse(cart.getCartId(),
                cart.getTotalPrice(),
                cart.getCartItems().stream().map(cartItem ->  new CartItemDto(cartItem.getProduct().getProductName(),cartItem.getProduct().getPrice(),cartItem.getProduct().getDiscountPrice(),cartItem.getQuantity(),cartItem.getProduct().getPercentageOfDiscount(),cartItem.getCartItemTotalPrice())).toList());

        return ServiceResult.success(response,String.format("Cart for %s",authUtil.loggedInUser().getUsername()));
    }

    @Override
    public ServiceResult<CartItemDto> updateQuantityInCartItem(Long productId, int operationValue) {

        Cart cart = cartRepository.getCartByEmail(authUtil.loggedInEmail());


        for(CartItem cartItem : cart.getCartItems()){
            if(cartItem.getProduct().getProductId().equals(productId)){
                cartItem.setQuantity(cartItem.getQuantity() + operationValue);

                if(cartItem.getQuantity() == 0){
                    cartItemRepository.delete(cartItem);
                    return ServiceResult.success(null , String.format("%s remove from Cart",cartItem.getProduct().getProductName()));
                }

                cartItemRepository.save(cartItem);

                return ServiceResult.success(new CartItemDto(cartItem.getProduct().getProductName(),cartItem.getProduct().getPrice(),cartItem.getProduct().getDiscountPrice(),cartItem.getQuantity(),cartItem.getProduct().getPercentageOfDiscount(),cartItem.getCartItemTotalPrice()),"Updated Quantity");
            }
        }

        throw new APIException("Product not found!");
    }

    @Override
    public ServiceResult<CartItemDto> deleteProductFromCart(Long productId) {
        Cart cart = cartRepository.getCartByEmail(authUtil.loggedInEmail());

        for(CartItem cartItem : cart.getCartItems()){
            if(cartItem.getProduct().getProductId().equals(productId)){
                cartItemRepository.delete(cartItem);

                Product product = cartItem.getProduct();

                CartItemDto itemDto = new CartItemDto(
                        product.getProductName(),
                        product.getPrice(),
                        product.getDiscountPrice(),
                        cartItem.getQuantity(),
                        product.getPercentageOfDiscount(),
                        cartItem.getCartItemTotalPrice());

                return ServiceResult.success(itemDto,String.format("%s remove from Cart",cartItem.getProduct().getProductName()));
            }
        }

        throw new APIException("Product not found!");
    }

    private Cart createCart() {
        Cart cart = cartRepository.getCartByEmail(authUtil.loggedInEmail());
        if(cart != null){
            return cart;
        }

        cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());

        Cart savedCart = cartRepository.save(cart);
        return savedCart;
    }

    private boolean stockControl(Integer quantity,Integer requestedQuantity){
        if(quantity >= requestedQuantity){
            return true;
        }
        return false;
    }

    private List<CartResponse> cartsMap(List<Cart> carts){
        List<CartResponse> responses = carts.stream()
                .map(cart -> {
                    List<CartItemDto> cartItemDtos = cart.getCartItems()
                            .stream()
                            .map(item -> {
                                Double productPrice = item.getProduct().getPrice();
                                Integer discount = item.getProduct().getPercentageOfDiscount();
                                Double discountedPrice = item.getProduct().getDiscountPrice();
                                Double totalItemPrice = discountedPrice * item.getQuantity();

                                return new CartItemDto(
                                        item.getProduct().getProductName(),
                                        productPrice,
                                        discountedPrice,
                                        item.getQuantity(),
                                        discount,
                                        totalItemPrice
                                );
                            })
                            .collect(Collectors.toList());

                    return new CartResponse(
                            cart.getCartId(),
                            cart.getTotalPrice(),
                            cartItemDtos
                    );
                })
                .toList();

        return responses;
    }
}
