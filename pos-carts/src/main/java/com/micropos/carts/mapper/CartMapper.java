package com.micropos.carts.mapper;


import com.micropos.carts.model.Cart;
import com.micropos.carts.model.CartItem;
import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.ProductDto;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface CartMapper {
    Cart toCart(CartDto cartDTO);
    CartItem toCartItem(CartItemDto cartItemDTO);
    CartDto toCartDto(Cart cart);

    default Mono<CartDto> toCartDto(Cart cart, Flux<CartItem> cartItems, Map<Integer, ProductDto> productMap) {
        CartDto cartDto = toCartDto(cart);
        return cartItems
                .map(cartItem -> toCartItemDto(cartItem, productMap.get(cartItem.getProductId())))
                .collectList()
                .map(cartItemDtos -> {
                    cartDto.setItems(cartItemDtos);
                    return cartDto;
                })
                .flatMap(Mono::just);
    }

    default CartItemDto toCartItemDto(CartItem cartItem, ProductDto productDto) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setAmount(cartItem.getAmount());
        cartItemDto.setProduct(productDto);
        return cartItemDto;
    }
}
