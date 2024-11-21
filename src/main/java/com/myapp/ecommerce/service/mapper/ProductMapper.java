package com.myapp.ecommerce.service.mapper;

import com.myapp.ecommerce.domain.Category;
import com.myapp.ecommerce.domain.Product;
import com.myapp.ecommerce.service.dto.CategoryDTO;
import com.myapp.ecommerce.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    ProductDTO toDto(Product s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
