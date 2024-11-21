package com.myapp.ecommerce.service.mapper;

import com.myapp.ecommerce.domain.Order;
import com.myapp.ecommerce.domain.OrderItem;
import com.myapp.ecommerce.domain.Product;
import com.myapp.ecommerce.service.dto.OrderDTO;
import com.myapp.ecommerce.service.dto.OrderItemDTO;
import com.myapp.ecommerce.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderItemDTO toDto(OrderItem s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
