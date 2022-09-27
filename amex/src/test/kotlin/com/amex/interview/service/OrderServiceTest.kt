package com.amex.interview.service

import com.amex.interview.exception.BadRequestException
import com.amex.interview.model.InventoryItem
import com.amex.interview.model.OrderSummary
import com.amex.interview.model.PricedItemOrder
import com.amex.interview.model.UserOrder
import com.amex.interview.util.TestHelper.Companion.myAny
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anySet
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class OrderServiceTest {

    @Mock
    lateinit var inventoryService:InventoryService

    @Mock
    lateinit var priceAdjustmentService: PriceAdjustmentService

    @InjectMocks
    lateinit var service: OrderService

    @BeforeEach
    fun doSetup() {

        `when`(inventoryService.lookupItemsByIds(anySet())).thenReturn(
            listOf(APPLE_INVENTORY, ORANGE_INVENTORY)
        )

        //Alternately we could open OrderSummary and mock it...
        val orderSummary = OrderSummary(UUID.randomUUID(),listOf(),listOf(),0)
        doAnswer { it.arguments[0] }.`when`(priceAdjustmentService).applyCurrentDeals(myAny(OrderSummary::class.java,orderSummary))

    }


    @Test
    fun successfullyCreate7AppleOrder() {
        val userOrder = UserOrder(listOf(SEVEN_APPLES))
        val orderSummary = service.createUserOrder(userOrder)
        assertEquals(1,orderSummary.pricedItems.size)
        assertEquals(PricedItemOrder(1,"Apple",60,7),
            orderSummary.pricedItems[0])
        assertEquals(orderSummary.totalPriceInCents,420)
    }

    @Test
    fun successfullyCreate7Apple2OrangeOrder() {
        val userOrder = UserOrder(listOf(SEVEN_APPLES, TWO_ORANGES))
        val orderSummary = service.createUserOrder(userOrder)
        assertEquals(2,orderSummary.pricedItems.size)
        assertEquals(PricedItemOrder(1,"Apple",60,7),
            orderSummary.pricedItems[0])
        assertEquals(PricedItemOrder(2,"Orange",25,2),
            orderSummary.pricedItems[1])
        assertEquals(orderSummary.totalPriceInCents,470)
    }

    @Test
    fun failsToOrderBecauseInsufficientInventory() {
        val tooLargeAnOrder = UserOrder(listOf(LARGE_APPLE_ORDER))
        val ex =assertThrows(BadRequestException::class.java) {
            service.createUserOrder(tooLargeAnOrder)
        }
        assertEquals("Insufficient inventory to order 7000 items with ID: 1 only: 1000 were available to order.",ex.message)
    }


    @Test
    fun failsOrderBecauseOrderIsEmpty() {
        val tooLargeAnOrder = UserOrder(listOf())
        val ex =assertThrows(BadRequestException::class.java) {
            service.createUserOrder(tooLargeAnOrder)
        }
        assertEquals("Should not receive an empty order!",ex.message)
    }


    @Test
    fun failsOrderBecauseOfIdNotInInventory() {
        val tooLargeAnOrder = UserOrder(listOf(NONEXISTENT_ORDER_ITEM))
        val ex =assertThrows(BadRequestException::class.java) {
            service.createUserOrder(tooLargeAnOrder)
        }
        assertEquals("Unable to find any item with the given Id: -1 (named: DoesNotExist)",ex.message)
    }

    companion object {
        val APPLE_INVENTORY = InventoryItem(1L, "Apple", 60, 1000)
        val ORANGE_INVENTORY = InventoryItem(2L, "Orange", 25, 500)

        val SEVEN_APPLES = PricedItemOrder(1,"Apple",60, 7)
        val TWO_ORANGES = PricedItemOrder(2,"Orange",25, 2)
        val LARGE_APPLE_ORDER = PricedItemOrder(1,"Apple",60, 7000)

        val NONEXISTENT_ORDER_ITEM = PricedItemOrder(-1,"DoesNotExist",10, 1)
    }
}