package com.myapp.ecommerce.domain;

import static com.myapp.ecommerce.domain.CategoryTestSamples.*;
import static com.myapp.ecommerce.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.ecommerce.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void productTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        category.addProduct(productBack);
        assertThat(category.getProducts()).containsOnly(productBack);
        assertThat(productBack.getCategory()).isEqualTo(category);

        category.removeProduct(productBack);
        assertThat(category.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getCategory()).isNull();

        category.products(new HashSet<>(Set.of(productBack)));
        assertThat(category.getProducts()).containsOnly(productBack);
        assertThat(productBack.getCategory()).isEqualTo(category);

        category.setProducts(new HashSet<>());
        assertThat(category.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getCategory()).isNull();
    }
}
