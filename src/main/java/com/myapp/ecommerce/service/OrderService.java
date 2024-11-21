package com.myapp.ecommerce.service;

import com.myapp.ecommerce.service.dto.OrderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.myapp.ecommerce.domain.Order}.
 */
public interface OrderService {
    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    OrderDTO save(OrderDTO orderDTO);

    /**
     * Updates a order.
     *
     * @param orderDTO the entity to update.
     * @return the persisted entity.
     */
    OrderDTO update(OrderDTO orderDTO);

    /**
     * Partially updates a order.
     *
     * @param orderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderDTO> partialUpdate(OrderDTO orderDTO);

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" order.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderDTO> findOne(Long id);

    /**
     * Delete the "id" order.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
