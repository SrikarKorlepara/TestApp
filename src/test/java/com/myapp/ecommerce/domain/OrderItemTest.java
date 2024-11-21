package com.myapp.ecommerce.domain;

import static com.myapp.ecommerce.domain.OrderItemTestSamples.*;
import static com.myapp.ecommerce.domain.OrderTestSamples.*;
import static com.myapp.ecommerce.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItem.class);
        OrderItem orderItem1 = getOrderItemSample1();
        OrderItem orderItem2 = new OrderItem();
        assertThat(orderItem1).isNotEqualTo(orderItem2);

        orderItem2.setId(orderItem1.getId());
        assertThat(orderItem1).isEqualTo(orderItem2);

        orderItem2 = getOrderItemSample2();
        assertThat(orderItem1).isNotEqualTo(orderItem2);
    }

    @Test
    void productTest() throws Exception {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        orderItem.setProduct(productBack);
        assertThat(orderItem.getProduct()).isEqualTo(productBack);

        orderItem.product(null);
        assertThat(orderItem.getProduct()).isNull();
    }

    @Test
    void orderTest() throws Exception {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderItem.setOrder(orderBack);
        assertThat(orderItem.getOrder()).isEqualTo(orderBack);

        orderItem.order(null);
        assertThat(orderItem.getOrder()).isNull();
    }
}
