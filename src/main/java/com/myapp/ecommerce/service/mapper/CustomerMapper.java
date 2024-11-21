package com.myapp.ecommerce.service.mapper;

import com.myapp.ecommerce.domain.Customer;
import com.myapp.ecommerce.domain.User;
import com.myapp.ecommerce.service.dto.CustomerDTO;
import com.myapp.ecommerce.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    CustomerDTO toDto(Customer s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
