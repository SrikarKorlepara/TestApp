package com.myapp.ecommerce.service.mapper;

import com.myapp.ecommerce.domain.Customer;
import com.myapp.ecommerce.domain.Order;
import com.myapp.ecommerce.service.dto.CustomerDTO;
import com.myapp.ecommerce.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    OrderDTO toDto(Order s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
