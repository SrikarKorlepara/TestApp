package com.myapp.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "total_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category", "orderItems" }, allowSetters = true)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orderItems", "customer" }, allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderItem totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderItem order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", totalPrice=" + getTotalPrice() +
            "}";
    }
}
