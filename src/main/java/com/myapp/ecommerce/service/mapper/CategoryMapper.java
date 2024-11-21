package com.myapp.ecommerce.service.mapper;

import com.myapp.ecommerce.domain.Category;
import com.myapp.ecommerce.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}
