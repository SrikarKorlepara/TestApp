package com.myapp.ecommerce.web.rest;

import static com.myapp.ecommerce.domain.OrderItemAsserts.*;
import static com.myapp.ecommerce.web.rest.TestUtil.createUpdateProxyForBean;
import static com.myapp.ecommerce.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.ecommerce.IntegrationTest;
import com.myapp.ecommerce.domain.OrderItem;
import com.myapp.ecommerce.repository.OrderItemRepository;
import com.myapp.ecommerce.service.dto.OrderItemDTO;
import com.myapp.ecommerce.service.mapper.OrderItemMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemMockMvc;

    private OrderItem orderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem().quantity(DEFAULT_QUANTITY).totalPrice(DEFAULT_TOTAL_PRICE);
        return orderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createUpdatedEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem().quantity(UPDATED_QUANTITY).totalPrice(UPDATED_TOTAL_PRICE);
        return orderItem;
    }

    @BeforeEach
    public void initTest() {
        orderItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        var returnedOrderItemDTO = om.readValue(
            restOrderItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderItemDTO.class
        );

        // Validate the OrderItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrderItem = orderItemMapper.toEntity(returnedOrderItemDTO);
        assertOrderItemUpdatableFieldsEquals(returnedOrderItem, getPersistedOrderItem(returnedOrderItem));
    }

    @Test
    @Transactional
    void createOrderItemWithExistingId() throws Exception {
        // Create the OrderItem with an existing ID
        orderItem.setId(1L);
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderItem.setQuantity(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderItem.setTotalPrice(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))));
    }

    @Test
    @Transactional
    void getOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get the orderItem
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItem
        OrderItem updatedOrderItem = orderItemRepository.findById(orderItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderItem are not directly saved in db
        em.detach(updatedOrderItem);
        updatedOrderItem.quantity(UPDATED_QUANTITY).totalPrice(UPDATED_TOTAL_PRICE);
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(updatedOrderItem);

        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderItemToMatchAllProperties(updatedOrderItem);
    }

    @Test
    @Transactional
    void putNonExistingOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem.quantity(UPDATED_QUANTITY).totalPrice(UPDATED_TOTAL_PRICE);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderItem, orderItem),
            getPersistedOrderItem(orderItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem.quantity(UPDATED_QUANTITY).totalPrice(UPDATED_TOTAL_PRICE);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderItemUpdatableFieldsEquals(partialUpdatedOrderItem, getPersistedOrderItem(partialUpdatedOrderItem));
    }

    @Test
    @Transactional
    void patchNonExistingOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderItem.setId(longCount.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderItem
        restOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderItemRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected OrderItem getPersistedOrderItem(OrderItem orderItem) {
        return orderItemRepository.findById(orderItem.getId()).orElseThrow();
    }

    protected void assertPersistedOrderItemToMatchAllProperties(OrderItem expectedOrderItem) {
        assertOrderItemAllPropertiesEquals(expectedOrderItem, getPersistedOrderItem(expectedOrderItem));
    }

    protected void assertPersistedOrderItemToMatchUpdatableProperties(OrderItem expectedOrderItem) {
        assertOrderItemAllUpdatablePropertiesEquals(expectedOrderItem, getPersistedOrderItem(expectedOrderItem));
    }
}
