package com.amex.interview.service

import com.amex.interview.exception.BadRequestException
import com.amex.interview.model.OrderSummary
import com.amex.interview.model.PricedItemOrder
import com.amex.interview.model.UserOrder
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService(val inventoryService: InventoryService,
                   val priceAdjustmentService: PriceAdjustmentService,
                   val orderRepository: OrderStore
) {

    // Might be denoted transactional if using a RDBMS... or have other transactional management
    // within...
    fun createUserOrder(userOrder: UserOrder): OrderSummary {
        val orderSummary=createUserOrderWithoutDeals(userOrder)
        val finalOrderSummary =priceAdjustmentService.applyCurrentDeals(orderSummary)
        orderRepository.save(finalOrderSummary)
        return finalOrderSummary
    }

    fun getUserOrderByUUID(uuid:UUID) = orderRepository.findById(uuid)


    // An unconstrained invocation isn't the best practice...
    // Normally we would constrain this somehow
    // (paging, a time-window, a LRU store that ejects oldest entries, etc.)
    fun getAllOrders() = orderRepository.findAll()


    private fun createUserOrderWithoutDeals(userOrder: UserOrder): OrderSummary {
        if (userOrder.orderedItems.isEmpty()) {
            throw BadRequestException("Should not receive an empty order!")
        }

        val userRequestedItemMappingById = userOrder.orderedItems.associateBy { it.itemId }
        val inventoryMappingById =
            inventoryService.lookupItemsByIds(userRequestedItemMappingById.keys)
                .associateBy { it.id }

        val pricedOrderItems = userRequestedItemMappingById.entries.map {
            val inventoryValue = inventoryMappingById[it.key]
                ?: throw BadRequestException(
                    "Unable to find any item with the given Id: ${it.key} (named: ${it.value.itemName})")

            if (inventoryValue.numberAvailable < it.value.numUnits) {
                throw BadRequestException(
                    "Insufficient inventory to order ${it.value.numUnits} items with ID: ${it.key} " +
                            "only: ${inventoryValue.numberAvailable} were available to order."
                )
            }

            PricedItemOrder(it.key,inventoryValue.name,
                inventoryValue.unitPriceInCents, it.value.numUnits)
        }

        val totalPriceInCents = pricedOrderItems.map {it.numUnits * it.unitPriceInCents}.sum()

        return OrderSummary(orderId = UUID.randomUUID(), pricedOrderItems, listOf(), totalPriceInCents)
    }
}