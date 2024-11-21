package com.myapp.ecommerce.domain;

import static com.myapp.ecommerce.domain.CategoryTestSamples.*;
import static com.myapp.ecommerce.domain.OrderItemTestSamples.*;
import static com.myapp.ecommerce.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.ecommerce.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void categoryTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }

    @Test
    void orderItemTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        product.addOrderItem(orderItemBack);
        assertThat(product.getOrderItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getProduct()).isEqualTo(product);

        product.removeOrderItem(orderItemBack);
        assertThat(product.getOrderItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getProduct()).isNull();

        product.orderItems(new HashSet<>(Set.of(orderItemBack)));
        assertThat(product.getOrderItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getProduct()).isEqualTo(product);

        product.setOrderItems(new HashSet<>());
        assertThat(product.getOrderItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getProduct()).isNull();
    }
}
